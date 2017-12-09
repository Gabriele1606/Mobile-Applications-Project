package com.example.gabri.firstapp;

import com.example.gabri.firstapp.Model.AppDatabase;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.util.List;
import java.util.UUID;

/**
 * Created by Gabri on 08/12/17.
 */

@Table(database = AppDatabase.class)
@Root(name="Images",strict = false)
public class Images {
    @PrimaryKey
    private int idGame;

    @ElementList(name = "fanart", inline = true, required = false)
    private List<Fanart> fanartList=null;

    @ElementList(name="boxart",inline = true, required = false)
    private List<Boxart> boxart=null;

    public int getIdGame() {
        return idGame;
    }

    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }

    public List<Fanart> getFanartList() {
        this.fanartList = SQLite.select().from(Fanart.class).where(Fanart_Table.idGame.eq(this.idGame)).queryList();
        return fanartList;
    }

    public void setFanartList(List<Fanart> fanartList) {
        this.fanartList = fanartList;
        save();
    }

    public List<Boxart> getBoxart() {
        this.boxart = SQLite.select().from(Boxart.class).where(Boxart_Table.idGame.eq(this.idGame)).queryList();
        return boxart;
    }

    public void setBoxart(List<Boxart> boxart) {
        this.boxart = boxart;
        save();
    }

    public void save(){
        final Images images = this;
        DatabaseDefinition databaseDefinition= FlowManager.getDatabase(AppDatabase.class);
        Transaction transactionImages= databaseDefinition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                FlowManager.getModelAdapter(Images.class).save(images);
            }
        }).build();
        transactionImages.execute();
        if (fanartList!=null){
            if (fanartList.size()>0){
                for (Fanart f :
                        fanartList) {
                    f.setIdFanart(UUID.randomUUID());
                    f.setIdGame(this.idGame);
                    f.save();
                }
            }
        }
        if (boxart!=null){
            if (boxart.size()>0){
                for (Boxart b :
                        boxart) {
                    b.setIdBoxart(UUID.randomUUID());
                    b.setIdGame(this.idGame);
                    b.save();
                }
            }
        }
    }
}
