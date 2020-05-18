package com.pfe.enginapp.repositories;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.pfe.enginapp.models.auth;
import com.pfe.enginapp.ui.Login;

import java.lang.ref.WeakReference;


public class authenticationRepository {

    private static final String IS_ADDING_NEW_ACCOUNT = "IS_ADDING_NEW_ACCOUNT" ;
    private static final String ACCOUNT_TYPE = "com.pfe.enginapp.account";
    private final static String AUTH_TOKEN_TYPE = "LoginAuthType" ;

    private final static String REMOTE_AUTH_SUCCESS = "remote_auth_success";

    private AccountManager accountManager ;

    private static authenticationRepository instance;
    private Context mContext;

    private auth currentUser;

    public static authenticationRepository getInstance(Context context){
        if(instance == null){
            instance = new authenticationRepository(context);
        }
        return  instance;
    }

    private authenticationRepository(Context context){
         this.mContext = context;

         accountManager = AccountManager.get(context); //context of the calling activity or fragment

    }



    public auth currentUser(){
        final String TAG =  "currentUser() :";

        if(currentUser == null){
            AccountManager am = AccountManager.get(mContext);

            Account[] accounts = am.getAccountsByType(ACCOUNT_TYPE);
            Account account ;
            if(accounts.length > 0){
                account = accounts[0];

            }else{
                //Toast.makeText(context,"No Account is Singed In",Toast.LENGTH_LONG).show();
                Log.d(TAG,"No Account is Singed In.");

                return null;
            }
            String authToken =am.peekAuthToken(account,AUTH_TOKEN_TYPE);

            if(authToken == null){

            }

            //Toast.makeText(context,"Token :"+auth,Toast.LENGTH_LONG).show();

        }

        return currentUser;

    }


    /**
     * Login() sends an authenticate the user remotely then save/update the local account
     * @param username
     * @param password
     */

    public void  Login(final String username, final String password){
        final String TAG =  "currentUser() :";

        //sends an authentication request to the server
        Intent inputIntent = new Intent();
        inputIntent.putExtra(auth.USERNAME,username);
        inputIntent.putExtra(auth.PASSWORD,password);


        new loginTask(this).execute(inputIntent);


    }

    /**
     * saveAccount() saves the user valid credentials locally in the android accounts
     * @param username
     * @param password
     * @param token the authentication token that will be used to serve http requests
     *
     *
     * */

    public Boolean saveAccount(String username,String password,String token){
        String TAG = "saveAccount() :";


        Account account = new Account(username,authenticationRepository.ACCOUNT_TYPE);


        boolean success = accountManager.addAccountExplicitly(account,password,null);

        if(success){

            Log.d(TAG, "Account created");
        }else{
            Log.d(TAG, "Account creation failed : (account already exist)");

            //update the password if changed
            if(!accountManager.getPassword(account).equals(password)){
                accountManager.setPassword(account,password);
                Log.d(TAG, "Account  password updated");
            }

        }

        //the account exist in the local database we need to update the token;
        accountManager.setAuthToken(account,authenticationRepository.AUTH_TOKEN_TYPE,token);
        Log.d(TAG, "Account  token updated");



        return success;
    }

    /**
     * refreshToken() sends an authentication request to the server
     * to obtain a new valid token
     *
     * */
    private auth refreshToken(){
        if(currentUser == null)
            return null;

        return  null;


    }

    private String getToken(){

        return  "";


    }

    public String getAuthToken(){

        return  "";

    }

    private static class loginTask extends AsyncTask<Intent, Void, Intent> {

        private final String TAG =  "currentUser() :";
        private final authenticationRepository authRepo;

        private WeakReference<authenticationRepository> repoRef;

        // only retain a weak reference to the activity
        loginTask(authenticationRepository authenticationRepository) {
            repoRef = new WeakReference<>(authenticationRepository);

            authRepo = repoRef.get();
        }

        @Override
        protected Intent doInBackground(Intent... inputIntent) {
            String username = inputIntent[0].getStringExtra(auth.USERNAME);
            String password = inputIntent[0].getStringExtra(auth.PASSWORD);


            String authToken ="nabi zakaria"; //sServerAuthenticate.userSignIn(userName, userPass, AUTH_TOKEN_TYPE);
            final Intent res = new Intent();

            //if remote authentication was successful
            Log.d(TAG, "doInBackground: ");
            res.putExtra(authenticationRepository.REMOTE_AUTH_SUCCESS, true);
            res.putExtra(auth.USERNAME, username);
            res.putExtra(auth.PASSWORD, password);
            res.putExtra(auth.TOKEN, authToken);

            //else
            //res.putExtra(authenticationRepository.REMOTE_AUTH_SUCCESS, false);


            return res;
        }
        @Override
        protected void onPostExecute(Intent intent) {
            Log.d(TAG, "onPostExecute: ");



            if(intent.getBooleanExtra(authenticationRepository.REMOTE_AUTH_SUCCESS,false)){
                String username = intent.getStringExtra(auth.USERNAME);
                String password = intent.getStringExtra(auth.PASSWORD);
                String token = intent.getStringExtra(auth.TOKEN);
                

                authRepo.saveAccount(username,password,token);


                ((Login)authRepo.mContext).authCompleted(true);

            }else{
                ((Login)authRepo.mContext).authCompleted(false);


            }

        }
    }

}
