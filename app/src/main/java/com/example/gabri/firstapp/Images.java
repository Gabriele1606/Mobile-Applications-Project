package com.example.gabri.firstapp;

import com.example.gabri.firstapp.Model.AppDatabase;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.util.List;

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
        return fanartList;
    }

    public void setFanartList(List<Fanart> fanartList) {
        this.fanartList = fanartList;
    }

    public List<Boxart> getBoxart() {
        return boxart;
    }

    public void setBoxart(List<Boxart> boxart) {
        this.boxart = boxart;
    }
}
