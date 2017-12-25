package com.example.gabri.firstapp.Model;

import com.example.gabri.firstapp.*;
import com.example.gabri.firstapp.GameDetail_Table;
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

import java.util.UUID;

/**
 * Created by Gabri on 12/11/17.
 */
@Table(database = AppDatabase.class)
@Root(name="Game", strict=false)
public class Game {
    @PrimaryKey
    @Element(name="id",required = false)
    private int id;

    @Column
    @Element(name="GameTitle",required = false)
    private String gameTitle;

    @Column(defaultValue = "1999")
    @Element(name="ReleaseDate",required = false)
    private String releaseDate="null";

    @Column(defaultValue = "unknown")
    @Element(name="Platform",required = false)
    private String platform;
    private int thumbnail;
    @Column(defaultValue = "1999")
    private int yearOfRelease;

    @Column
    private int idPlatform;

    private GameDetail gameDetail=null;

    private boolean gameHasFanart=false;
    private boolean gameHasBoxart=false;

    private String idForFirebase;


    public Game(String s, int i, int cover) {
        gameTitle=s;
        thumbnail=cover;
    }
    public void setIdPlatform(int idPlatform) {
        this.idPlatform = idPlatform;
    }

    public int getIdPlatform() {
        return idPlatform;
    }
    public Game() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public boolean releaseDateIsPresent(){
        if (this.releaseDate.equals(null))
            return false;
        return true;
    }

    public int getYearOfRelease() {
        return yearOfRelease;
    }

    public void setYearOfRelease(int yearOfRelease) {
        this.yearOfRelease = yearOfRelease;
    }

    public GameDetail getGameDetail() {
        this.gameDetail=SQLite.select().from(GameDetail.class).where(GameDetail_Table.id.eq(this.id)).querySingle();
        return this.gameDetail;
    }

    public void setGameDetail(final GameDetail gameDetail) {
        DatabaseDefinition databaseDefinition= FlowManager.getDatabase(AppDatabase.class);
        Transaction transactionGameDetail= databaseDefinition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                FlowManager.getModelAdapter(GameDetail.class).save(gameDetail);
            }
        }).build();
        transactionGameDetail.execute();
        this.gameDetail = gameDetail;
    }

    public boolean hasGameFanart() {
        return gameHasFanart;
    }

    public void setGameHasFanart(boolean gameHasFanart) {
        this.gameHasFanart = gameHasFanart;
    }

    public boolean hasGameBoxart() {
        return gameHasBoxart;
    }

    public void setGameHasBoxart(boolean gameHasBoxart) {
        this.gameHasBoxart = gameHasBoxart;
    }

    public String getIdForFirebase() {
        return idForFirebase;
    }

    public void setIdForFirebase(String idForFirebase) {
        this.idForFirebase = idForFirebase;
    }
}
