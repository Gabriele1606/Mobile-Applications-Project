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

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.UUID;

/**
 * Created by Gabri on 07/12/17.
 */
@Table(database = AppDatabase.class)
@Root(name="fanart", strict=false)
public class Fanart {


    @PrimaryKey
    private UUID idFanart;


    @Column
    private int idGame;

    @Column
    @Element(name="original",required = false)
    private String originalFanart;

    @Column
    @Element(name="thumb",required = false)
    private String thumb;

    public void setIdFanart(UUID idFanart) {
        this.idFanart = idFanart;
    }
    public UUID getIdFanart() {
        return idFanart;
    }
    public int getIdGame() {
        return idGame;
    }

    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }
    public String getOriginalFanart() {
        return originalFanart;
    }

    public void setOriginalFanart(String originalFanart) {
        this.originalFanart = originalFanart;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public void save(){
        final Fanart fanart = this;
        DatabaseDefinition databaseDefinition= FlowManager.getDatabase(AppDatabase.class);
        Transaction transactionFanart= databaseDefinition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                FlowManager.getModelAdapter(Fanart.class).save(fanart);
            }
        }).build();
        transactionFanart.execute();
    }
}
