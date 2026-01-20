package com.betopia.hrm.cdc_data_pipeline.domain.models;

public class Source {
    private String version;
    private String name;
    private int server_id;
    private int ts_sec;
    private Object gtid;
    private String file;
    private int pos;
    private int row;
    private boolean snapshot;
    private Object thread;
    private String db;
    private String table;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServer_id() {
        return server_id;
    }

    public void setServer_id(int server_id) {
        this.server_id = server_id;
    }

    public int getTs_sec() {
        return ts_sec;
    }

    public void setTs_sec(int ts_sec) {
        this.ts_sec = ts_sec;
    }

    public Object getGtid() {
        return gtid;
    }

    public void setGtid(Object gtid) {
        this.gtid = gtid;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public boolean isSnapshot() {
        return snapshot;
    }

    public void setSnapshot(boolean snapshot) {
        this.snapshot = snapshot;
    }

    public Object getThread() {
        return thread;
    }

    public void setThread(Object thread) {
        this.thread = thread;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
