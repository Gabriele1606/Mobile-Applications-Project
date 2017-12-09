package com.example.gabri.firstapp;

import com.example.gabri.firstapp.Model.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.util.UUID;

/**
 * Created by Gabri on 08/12/17.
 */
@Table(database = AppDatabase.class)
@Root(strict = false)
public class Boxart {

    @PrimaryKey
    private UUID idBoxart;

    @Column
    private int idGame;

    @Column
    @Attribute(name = "side")
    private String side;

    @Column
    @Attribute(name = "thumb")
    private String thumb;

    public void setIdBoxart(UUID idBoxart) {
        this.idBoxart = idBoxart;
    }
    public UUID getIdBoxart() {
        return idBoxart;
    }
    public int getIdGame() {
        return idGame;
    }

    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public void save(){
        final Boxart boxart = this;
        DatabaseDefinition databaseDefinition= FlowManager.getDatabase(AppDatabase.class);
        Transaction transactionBoxart= databaseDefinition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                FlowManager.getModelAdapter(Boxart.class).save(boxart);
            }
        }).build();
        transactionBoxart.execute();
    }
}
