package com.zxjk.duoduo.network.response;

import java.util.List;

public class GetRebateDetailsResponse {

    /**
     * total : 2
     * list : [{"id":"","groupId":"","customerId":"","inviterId":"","isOwner":"","grade":"","directNum":"","teamNum":"","currentDirectPer":"","currentTeamPer":"","rebateRate":"","rebateAmount":"0.2250","rebateTotalAmount":""},{"id":"","groupId":"","customerId":"","inviterId":"","createDate":1555413178259,"isOwner":"","grade":"","directNum":"","teamNum":"","currentDirectPer":"","currentTeamPer":"","rebateRate":"","rebateAmount":"0.2250","rebateTotalAmount":""}]
     * pageNum : 1
     * pageSize : 10
     * size : 2
     * startRow : 1
     * endRow : 2
     * pages : 1
     * prePage : 0
     * nextPage : 0
     * isFirstPage : true
     * isLastPage : true
     * hasPreviousPage : false
     * hasNextPage : false
     * navigatePages : 8
     * navigatepageNums : [1]
     * navigateFirstPage : 1
     * navigateLastPage : 1
     * firstPage : 1
     * lastPage : 1
     */

    private int total;
    private int pageNum;
    private int pageSize;
    private int size;
    private int startRow;
    private int endRow;
    private int pages;
    private int prePage;
    private int nextPage;
    private boolean isFirstPage;
    private boolean isLastPage;
    private boolean hasPreviousPage;
    private boolean hasNextPage;
    private int navigatePages;
    private int navigateFirstPage;
    private int navigateLastPage;
    private int firstPage;
    private int lastPage;
    private List<ListBean> list;
    private List<Integer> navigatepageNums;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public boolean isIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public boolean isIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public int getNavigateFirstPage() {
        return navigateFirstPage;
    }

    public void setNavigateFirstPage(int navigateFirstPage) {
        this.navigateFirstPage = navigateFirstPage;
    }

    public int getNavigateLastPage() {
        return navigateLastPage;
    }

    public void setNavigateLastPage(int navigateLastPage) {
        this.navigateLastPage = navigateLastPage;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<Integer> getNavigatepageNums() {
        return navigatepageNums;
    }

    public void setNavigatepageNums(List<Integer> navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
    }

    public static class ListBean {
        /**
         * id :
         * groupId :
         * customerId :
         * inviterId :
         * isOwner :
         * grade :
         * directNum :
         * teamNum :
         * currentDirectPer :
         * currentTeamPer :
         * rebateRate :
         * rebateAmount : 0.2250
         * rebateTotalAmount :
         * createDate : 1555413178259
         */

        private String id;
        private String groupId;
        private String customerId;
        private String inviterId;
        private String isOwner;
        private String grade;
        private String directNum;
        private String teamNum;
        private String currentDirectPer;
        private String currentTeamPer;
        private String rebateRate;
        private String rebateAmount;
        private String rebateTotalAmount;
        private long createDate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getInviterId() {
            return inviterId;
        }

        public void setInviterId(String inviterId) {
            this.inviterId = inviterId;
        }

        public String getIsOwner() {
            return isOwner;
        }

        public void setIsOwner(String isOwner) {
            this.isOwner = isOwner;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getDirectNum() {
            return directNum;
        }

        public void setDirectNum(String directNum) {
            this.directNum = directNum;
        }

        public String getTeamNum() {
            return teamNum;
        }

        public void setTeamNum(String teamNum) {
            this.teamNum = teamNum;
        }

        public String getCurrentDirectPer() {
            return currentDirectPer;
        }

        public void setCurrentDirectPer(String currentDirectPer) {
            this.currentDirectPer = currentDirectPer;
        }

        public String getCurrentTeamPer() {
            return currentTeamPer;
        }

        public void setCurrentTeamPer(String currentTeamPer) {
            this.currentTeamPer = currentTeamPer;
        }

        public String getRebateRate() {
            return rebateRate;
        }

        public void setRebateRate(String rebateRate) {
            this.rebateRate = rebateRate;
        }

        public String getRebateAmount() {
            return rebateAmount;
        }

        public void setRebateAmount(String rebateAmount) {
            this.rebateAmount = rebateAmount;
        }

        public String getRebateTotalAmount() {
            return rebateTotalAmount;
        }

        public void setRebateTotalAmount(String rebateTotalAmount) {
            this.rebateTotalAmount = rebateTotalAmount;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }
    }
}
