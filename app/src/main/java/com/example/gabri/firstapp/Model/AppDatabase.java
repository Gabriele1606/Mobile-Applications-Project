package com.example.gabri.firstapp.Model;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by simon on 07/12/2017.
 */

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {

    public static final String NAME = "DBGAMES";

    public static final int VERSION = 1;
}