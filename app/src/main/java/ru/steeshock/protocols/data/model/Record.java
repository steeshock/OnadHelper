package ru.steeshock.protocols.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Record {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int mId;

    @ColumnInfo(name = "protocol_number")
    private String mProtocolNumber;

    @ColumnInfo(name = "act_number")
    private String mActNumber;

    @ColumnInfo(name = "description")
    private String mDescription;

    @ColumnInfo(name = "status_str")
    private String mStatusStr;

    @ColumnInfo(name = "status_num")
    private long mStatusNum;

    @ColumnInfo(name = "first_date")
    private long mFirstDate;

    @ColumnInfo(name = "last_date")
    private long mLastDate;

    @ColumnInfo(name = "user_token")
    private String mUserToken;

    public Record() {
    }

    // Конструктор для обновления записи
    public Record(int id, String protocolNumber, String actNumber, String description, String statusStr, long statusNum, long firstDate, long lastDate, String token) {
        mId = id;
        mProtocolNumber = protocolNumber;
        mActNumber = actNumber;
        mDescription = description;
        mStatusStr = statusStr;
        mStatusNum = statusNum;
        mLastDate = lastDate;
        mFirstDate = firstDate;
        mUserToken = token;
    }

    // Конструктор для создания новой записи, ID генерируется автоматически
    public Record(String protocolNumber, String actNumber, String description, String statusStr, long statusNum, long firstDate, String token) {
        mProtocolNumber = protocolNumber;
        mActNumber = actNumber;
        mDescription = description;
        mStatusStr = statusStr;
        mStatusNum = statusNum;
        mFirstDate = firstDate;
        mUserToken = token;
    }

    // Конструктор для создания новой расширенной записи
    public Record(String protocolNumber, String actNumber, String description, String statusStr, long statusNum, long firstDate, long lastDate, String token) {
        mProtocolNumber = protocolNumber;
        mActNumber = actNumber;
        mDescription = description;
        mStatusStr = statusStr;
        mStatusNum = statusNum;
        mFirstDate = firstDate;
        mLastDate = lastDate;
        mUserToken = token;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getProtocolNumber() {
        return mProtocolNumber;
    }

    public void setProtocolNumber(String protocolNumber) {
        mProtocolNumber = protocolNumber;
    }

    public String getActNumber() {
        return mActNumber;
    }

    public void setActNumber(String actNumber) {
        mActNumber = actNumber;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getStatusStr() {
        return mStatusStr;
    }

    public void setStatusStr(String statusStr) {
        mStatusStr = statusStr;
    }

    public long getStatusNum() {
        return mStatusNum;
    }

    public void setStatusNum(long statusNum) {
        mStatusNum = statusNum;
    }

    public String getUserToken() {
        return mUserToken;
    }

    public void setUserToken(String userToken) {
        mUserToken = userToken;
    }

    public long getFirstDate() {
        return mFirstDate;
    }

    public void setFirstDate(long mFirstDate) {
        this.mFirstDate = mFirstDate;
    }

    public long getLastDate() {
        return mLastDate;
    }

    public void setLastDate(long mLastDate) {
        this.mLastDate = mLastDate;
    }
}

