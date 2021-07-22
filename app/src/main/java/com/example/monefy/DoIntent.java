package com.example.monefy;

import android.content.Context;
import android.content.Intent;

public class DoIntent {
   static DoIntent doIntent = new DoIntent();
   private Intent intent = new Intent();

 public static DoIntent getInstance(){
     return  doIntent;
 }

    public  Intent getDoIntent(){
    return intent;
    }
    public  void setDoIntent(Context context, Class<?> cls){
        intent.setClass(context, cls);


    }

    public static Intent startactivity(Context from, Class<?> cClass, int check) {
        DoIntent doIntent = DoIntent.getInstance();
        doIntent.setDoIntent(from, cClass);
        Intent intent = doIntent.getDoIntent();
        intent.putExtra("fragmentNumber",check);
        return intent;
    }

}
