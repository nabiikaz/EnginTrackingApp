package com.pfe.enginapp.services;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.pfe.enginapp.models.Agent;
import com.pfe.enginapp.models.Auth;
import com.pfe.enginapp.ui.Dashboard.Dashboard;
import com.pfe.enginapp.ui.Login;
import com.pfe.enginapp.ui.MainActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class AuthenticationService {


    private static final String IS_ADDING_NEW_ACCOUNT = "IS_ADDING_NEW_ACCOUNT";
    private static final String ACCOUNT_TYPE = "com.pfe.enginapp.account";
    private final static String AUTH_TOKEN_TYPE = "LoginAuthType";

    private final static String REMOTE_AUTH_SUCCESS = "remote_auth_success";
    private static final String TAG = "authService";
    private static final String AUTHENTICATE_USING_TOKEN = "authenticate_using_token";
    private static final String REMOTE_TOKEN_CHECK_SUCCESS = "remote_token_check_success";

    public final static String AUTH_EXCEPTION = "auth_exception";
    public static final String AUTH_INVALID_CREDENTIALS = "auth_invalid_credentials";
    public static final String AUTH_INVALID_TOKEN = "auth_invalid_token";
    private static final String REFRESH_TOKEN = "refresh_token";
    public static final int LOGIN_TO_AUTHENTICATE = 0;
    public static final int LOGIN_TO_REFRESH_TOKEN = 1;


    public static  final String LOGIN_ACTIVITY = Login.class.getSimpleName();
    public static final String MAIN_ACTIVITY = MainActivity.class.getSimpleName();
    public static final String DASHBOARD_ACTIVITY = Dashboard.class.getSimpleName();


    //this no_activity is used when authenticate() is called
    public static final String NO_ACTIVITY = "0";


    //private static authenticationService instance;
    private  Context mContext;
    private  String currentActivity;
    private static AccountManager accountManager;


    private Auth currentUser;



    public AuthenticationService(Context context,String currentActivity) {

        if (mContext == null) {
            this.currentActivity = currentActivity;
            mContext = context;
            accountManager = AccountManager.get(context); //context of the calling activity or fragment
        }


    }


    /**
     * @return the current user in cache
     */
    public Auth getCurrentUser() {
        Log.d(TAG, "getCurrentUser:");


        AccountManager am = AccountManager.get(mContext);
        Account[] accounts = am.getAccountsByType(ACCOUNT_TYPE);
        Account account;


        if (accounts.length == 0) {

            Log.d(TAG, "currentUser : No Account is Singed In.");
            if (!currentActivity.equals(LOGIN_ACTIVITY)) {
                Intent intent = new Intent(mContext, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mContext.startActivity(intent);
            }

            return null;


        }

        //this authentication configuration allows only one user account
        account = accounts[0];


        String authToken = am.peekAuthToken(account, AUTH_TOKEN_TYPE);


        if (authToken == null) {
            //the account is registered in the android accounts but no Token has been assigned to it
            //refreshToken(account.name, am.getPassword(account));
        }


        String password = am.getPassword(account);
        return new Auth(account.name, password, authToken);


    }


    /**
     * Login() sends an authenticate the user remotely then save/update the local account
     *
     * @param username
     * @param password
     *
     */

    public void Login(final String username, final String password) {
        final String TAG = "Login() :";




        Retrofit retrofit = new retrofitClient().getRetrofit();

        final IUserClient userClient = retrofit.create(IUserClient.class);
        final Auth auth = new Auth(username, password);

        Call<Agent> call = userClient.login(auth);


        call.enqueue(new Callback<Agent>() {
            @Override
            public void onResponse(Call<Agent> call, Response<Agent> response) {

                if (response.isSuccessful()) {

                    Intent userdata = new Intent();


                    userdata.putExtra(Agent.AGENT_ID, response.body().getAgent_id());
                    userdata.putExtra(Agent.AGENT_NOM, response.body().getAgent_nom());
                    userdata.putExtra(Agent.AGENT_ROLE, response.body().getAgent_role());
                    userdata.putExtra(Auth.USERNAME, username);
                    userdata.putExtra(Auth.PASSWORD, password);
                    userdata.putExtra(Auth.TOKEN, response.headers().get("Authorization"));






                    //saving the account
                    saveAccount(username, password, userdata.getExtras());



                    //update the login activity ui
                    Intent dashboardIntent = new Intent(mContext, Dashboard.class);
                    dashboardIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    mContext.startActivity(dashboardIntent);





                } else {
                    Log.d(TAG, "onResponse: "+currentActivity);

                    ((Login) mContext).showAuthenticationErrors(Login.AUTH_INVALID_CREDENTIALS);
                }

            }

            @Override
            public void onFailure(Call<Agent> call, Throwable t) {

            }
        });


    }

    /**
     * saveAccount() saves the user valid credentials locally in the android accounts
     *
     * @param username
     * @param password
     */

    private Boolean saveAccount(String username, String password, Bundle userdata) {
        Log.d(TAG, "saveAccount: ");


        Account account = new Account(username, AuthenticationService.ACCOUNT_TYPE);


        boolean success = accountManager.addAccountExplicitly(account, password, userdata);



        if (success) {

            Log.d(TAG, "Account created");
        } else {

            Log.d(TAG, "Account creation failed : (account already exist)");

            //update the password if changed
            if (!accountManager.getPassword(account).equals(password)) {
                accountManager.setPassword(account, password);
                Log.d(TAG, "Account  password updated");
            }

            //update userdata
            Log.d(TAG, "userdata update NOT IMPLEMENTED");


        }

        String token = userdata.getString(Auth.TOKEN);
        //the account exist in the local database we need to update the token;
        accountManager.setAuthToken(account, AuthenticationService.AUTH_TOKEN_TYPE, token);
        Log.d(TAG, "Account  token updated");





        return success;
    }



    /**
     * This method won't execute the full logic of until unless the mContext is instanceof Login activity
     *
     * @param auth_status status of the authentication
     */
    private void loginActivityAuthCompleted(Boolean auth_status) {



        if (auth_status) {


            Intent dashboardIntent = new Intent(mContext, Dashboard.class);
            dashboardIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            mContext.startActivity(dashboardIntent);


            ((Login) mContext).finish();


        } else {
            ((Login) mContext).showAuthenticationErrors(Login.AUTH_INVALID_CREDENTIALS);
        }
    }



    /**
     * checkToken checks for the validity of the token in the remote server
     *
     *
     * @return
     */
    public void authenticateWithRedirection(final String activity_name) {
        Log.d(TAG, "checkToken: ");

        final String authToken = getCurrentUser().getAuthToken();
        Retrofit retrofit = new retrofitClient().getRetrofit();

        IUserClient userClient = retrofit.create(IUserClient.class);


        Call<ResponseBody> call = userClient.checkToken(authToken);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    if(activity_name.equals(AuthenticationService.DASHBOARD_ACTIVITY)){

                        Intent intent = new Intent(mContext,Dashboard.class) ;
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        mContext.startActivity(intent);
                    }










                } else {


                   if(activity_name.equals(AuthenticationService.LOGIN_ACTIVITY))
                        return;



                    Intent intent = new Intent(mContext, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    mContext.startActivity(intent);
                    ((Activity)mContext).finish();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }



    public void Logout(){

        AccountManager accountManager = AccountManager.get(mContext);

        String authToken = getCurrentUser().getAuthToken();

        accountManager.invalidateAuthToken(AuthenticationService.ACCOUNT_TYPE,authToken);

        Log.d(TAG, "Logout: "+currentActivity.equals(Login.class.getSimpleName()));
        Log.d(TAG, "Logout: new AuthToken "+getCurrentUser().getAuthToken());
        if(!currentActivity.equals(AuthenticationService.LOGIN_ACTIVITY)){

            Intent intent = new Intent(mContext,Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            mContext.startActivity(intent);

        }

    }


    public String getAuthToken(){
        return getCurrentUser().getAuthToken();
    }
}
