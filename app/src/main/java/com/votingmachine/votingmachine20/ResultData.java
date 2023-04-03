package com.votingmachine.votingmachine20;

public class ResultData {

    private String userName;
    private String userVotes;

    public ResultData(String userName, String userVotes) {
        this.userName = userName;
        this.userVotes = userVotes;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserVotes() {
        return userVotes;
    }

    public void setUserVotes(String userVotes) {
        this.userVotes = userVotes;
    }
}
