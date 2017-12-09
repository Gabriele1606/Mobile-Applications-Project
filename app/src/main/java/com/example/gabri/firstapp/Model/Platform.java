package com.example.gabri.firstapp.Model;

import com.example.gabri.firstapp.Boxart;
import com.example.gabri.firstapp.Fanart;
import com.example.gabri.firstapp.GameDetail;
import com.example.gabri.firstapp.PlatformDetail;
import com.example.gabri.firstapp.PlatformDetail_Table;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Gabri on 16/11/17.
 */
@Table(database = AppDatabase.class)
@Root(name="Platform", strict=false)
public class Platform {

    @PrimaryKey
    @Element(name="id",required = false)
    private int id;

    @Column(defaultValue = "unknown")
    @Element(name="name",required = false)
    private String name;

    @Column(defaultValue = "unknown")
    @Element(name="alias",required = false)
    private String alias;

    @Column(defaultValue = "1999")
    private int averageYearOfItsGame;


    private PlatformDetail platformDetail;

    private List<Game> gameList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getAverageYearOfItsGame() {
        return averageYearOfItsGame;
    }

    public void setAverageYearOfItsGame(int averageYearOfItsGame) {
        this.averageYearOfItsGame = averageYearOfItsGame;
    }

    public PlatformDetail getPlatformDetail() {
        this.platformDetail = SQLite.select().from(PlatformDetail.class).where(PlatformDetail_Table.id.eq(this.id)).querySingle();
        return platformDetail;
    }

    public void setPlatformDetail(PlatformDetail platformDetail) {
        this.platformDetail = platformDetail;
    }

    public List<Game> getGameList() {
        this.gameList = SQLite.select().from(Game.class).where(Game_Table.idPlatform.eq(this.id)).queryList();
        return gameList;
    }

    public void setGameList(List<Game> gameList) {
        this.gameList = gameList;
    }

    public void updateGame(){
        final ArrayList<Game> objects = new ArrayList<Game>();
        objects.addAll(this.gameList);
        for (Game g :
                objects) {
            g.setIdPlatform(this.id);
        }
        DatabaseDefinition databaseDefinition= FlowManager.getDatabase(AppDatabase.class);
        Transaction transactionGame= databaseDefinition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                for (Game g :
                        objects) {
                    FlowManager.getModelAdapter(Game.class).save(g);
                }
            }
        }).build();
        transactionGame.execute();

        final ArrayList<GameDetail> gameDetails = new ArrayList<>();
        for (Game g :
                objects) {
            gameDetails.add(g.getGameDetail());
        }

        Transaction transactionGameDetail= databaseDefinition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                for (GameDetail gd :
                        gameDetails) {
                    if (gd!=null){
                        FlowManager.getModelAdapter(GameDetail.class).save(gd);
                        if (gd.getImages()!=null)
                            gd.getImages().save();
                    }
                }
            }
        }).build();
        transactionGameDetail.execute();

    }
}

