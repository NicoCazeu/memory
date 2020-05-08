package com.softcaze.memory.inappbilling;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;
import com.softcaze.memory.listener.BoughtItemListener;
import com.softcaze.memory.service.Timer;
import com.softcaze.memory.util.ApplicationConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class BillingManager {
    private static BillingManager ourInstance = new BillingManager();
    protected IInAppBillingService mService;
    private Activity activity;
    protected int counter;
    protected Timer timer;
    protected BoughtItemListener boughtItemListener;

    public static BillingManager getInstance() {
        return ourInstance;
    }

    private BillingManager() {
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void initialize(final TextView pricePack1, final TextView pricePack2, BoughtItemListener listener) {
        this.boughtItemListener = listener;
        // Payment etc
        ServiceConnection mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
            }
            @Override
            public void onServiceConnected(ComponentName name,
                                           IBinder service) {
                mService = IInAppBillingService.Stub.asInterface(service);

                try {
                    updatePriceItems(pricePack1, pricePack2);
                } catch (JSONException je) {
                    je.printStackTrace();
                } catch (RemoteException re) {
                    re.printStackTrace();
                }
            }
        };

        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        activity.bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
    }

    /**
     * Update prices of items
     * @param pricePack1
     * @param pricePack2
     * @throws RemoteException
     * @throws JSONException
     */
    private void updatePriceItems(TextView pricePack1, TextView pricePack2) throws RemoteException, JSONException {
        ArrayList<String> listItems = new ArrayList<>(Arrays.asList(ApplicationConstants.IN_APP_BILLING_ITEM_1, ApplicationConstants.IN_APP_BILLING_ITEM_2));
        Bundle query = new Bundle();
        query.putStringArrayList("ITEM_ID_LIST", listItems);

        if(mService != null) {
            Bundle skuDetails = mService.getSkuDetails(3, activity.getPackageName(), "inapp", query);

            ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");
            if(responseList != null) {
                for (String responseItem : responseList) {
                    JSONObject jsonObject = new JSONObject(responseItem);

                    if (jsonObject.getString("productId") != null) {
                        if (jsonObject.getString("productId").equals(ApplicationConstants.IN_APP_BILLING_ITEM_1)) {
                            if (jsonObject.getString("price") != null) {
                                pricePack1.setText(jsonObject.getString("price"));
                            }
                        } else if (jsonObject.get("productId").equals(ApplicationConstants.IN_APP_BILLING_ITEM_2)) {
                            if (jsonObject.getString("price") != null) {
                                pricePack2.setText(jsonObject.getString("price"));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Pruchase an item
     * @param idProduct
     */
    public void tryToPurchase(String idProduct) {
        Bundle buyIntentBundle = null;
        try {
            buyIntentBundle = mService.getBuyIntent(3, activity.getPackageName(), idProduct, "inapp", "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
            PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
            if (pendingIntent == null) {
                return;
            }
            activity.startIntentSenderForResult(pendingIntent.getIntentSender(), 1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            if (resultCode == Activity.RESULT_OK) {
                try {
                    JSONObject jo = new JSONObject(purchaseData);
                    String sku = jo.optString("productId");
                    String packageName = jo.optString("packageName");
                    String purchaseToken = jo.optString("purchaseToken");

                    if (sku.equalsIgnoreCase(ApplicationConstants.IN_APP_BILLING_ITEM_1)) {
                        mService.consumePurchase(3, packageName, purchaseToken);
                        this.boughtItemListener.boughtItem(sku);
                    }
                    else if (sku.equalsIgnoreCase(ApplicationConstants.IN_APP_BILLING_ITEM_2)) {
                        mService.consumePurchase(3, packageName, purchaseToken);
                        this.boughtItemListener.boughtItem(sku);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if(resultCode == Activity.RESULT_CANCELED) {
                this.boughtItemListener.itemCanceled();
            }
        }
    }
}
