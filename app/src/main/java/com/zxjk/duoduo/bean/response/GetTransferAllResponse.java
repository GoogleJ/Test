package com.zxjk.duoduo.bean.response;

import java.io.Serializable;
import java.util.List;

public class GetTransferAllResponse {

    /**
     * total : 1
     * list : [{"id":"","customerId":"","duoduoId":"","blockNumber":"","createTime":"1552911912","transactionHash":"0x84f5a2ee32212ca29fa6e420faf235fd579803d1009e9d2ab1cf1cc2807ca15d","nonce":"","blockHash":"","fromAddress":"0x51F027C78d4bF6bf622E5A02EfBdB395850beD84","toAddress":"0x93E996F6199ADb4aE66Accbe99Ff3876571B0909","balance":"100000000000000000","gas":"","gasPrice":"","isError":"","txreceiptStatus":"2","contractAddress":"","gasUsed":"0.000021004","isDelete":"","tokenName":"","tokenSymbol":"HKB","tokenDecimal":"","serialType":"1","inOrOut":"2"}]
     * pageNum : 1
     * pageSize : 1
     * size : 1
     * startRow : 0
     * endRow : 6
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

    public static class ListBean implements Serializable {
        /**
         * id :
         * customerId :
         * duoduoId :
         * blockNumber :
         * createTime : 1552911912
         * transactionHash : 0x84f5a2ee32212ca29fa6e420faf235fd579803d1009e9d2ab1cf1cc2807ca15d
         * nonce :
         * blockHash :
         * fromAddress : 0x51F027C78d4bF6bf622E5A02EfBdB395850beD84
         * toAddress : 0x93E996F6199ADb4aE66Accbe99Ff3876571B0909
         * balance : 100000000000000000
         * gas :
         * gasPrice :
         * isError :
         * txreceiptStatus : 2
         * contractAddress :
         * gasUsed : 0.000021004
         * isDelete :
         * tokenName :
         * tokenSymbol : HKB
         * tokenDecimal :
         * serialType : 1
         * inOrOut : 2
         */

        private String id;
        private String customerId;
        private String duoduoId;
        private String blockNumber;
        private String createTime;
        private String transactionHash;
        private String nonce;
        private String blockHash;
        private String fromAddress;
        private String toAddress;
        private String balance;
        private String gas;
        private String gasPrice;
        private String isError;
        private String txreceiptStatus;
        private String contractAddress;
        private String gasUsed;
        private String isDelete;
        private String tokenName;
        private String tokenSymbol;
        private String tokenDecimal;
        private String serialType;
        private String inOrOut;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getDuoduoId() {
            return duoduoId;
        }

        public void setDuoduoId(String duoduoId) {
            this.duoduoId = duoduoId;
        }

        public String getBlockNumber() {
            return blockNumber;
        }

        public void setBlockNumber(String blockNumber) {
            this.blockNumber = blockNumber;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getTransactionHash() {
            return transactionHash;
        }

        public void setTransactionHash(String transactionHash) {
            this.transactionHash = transactionHash;
        }

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public String getBlockHash() {
            return blockHash;
        }

        public void setBlockHash(String blockHash) {
            this.blockHash = blockHash;
        }

        public String getFromAddress() {
            return fromAddress;
        }

        public void setFromAddress(String fromAddress) {
            this.fromAddress = fromAddress;
        }

        public String getToAddress() {
            return toAddress;
        }

        public void setToAddress(String toAddress) {
            this.toAddress = toAddress;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getGas() {
            return gas;
        }

        public void setGas(String gas) {
            this.gas = gas;
        }

        public String getGasPrice() {
            return gasPrice;
        }

        public void setGasPrice(String gasPrice) {
            this.gasPrice = gasPrice;
        }

        public String getIsError() {
            return isError;
        }

        public void setIsError(String isError) {
            this.isError = isError;
        }

        public String getTxreceiptStatus() {
            return txreceiptStatus;
        }

        public void setTxreceiptStatus(String txreceiptStatus) {
            this.txreceiptStatus = txreceiptStatus;
        }

        public String getContractAddress() {
            return contractAddress;
        }

        public void setContractAddress(String contractAddress) {
            this.contractAddress = contractAddress;
        }

        public String getGasUsed() {
            return gasUsed;
        }

        public void setGasUsed(String gasUsed) {
            this.gasUsed = gasUsed;
        }

        public String getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(String isDelete) {
            this.isDelete = isDelete;
        }

        public String getTokenName() {
            return tokenName;
        }

        public void setTokenName(String tokenName) {
            this.tokenName = tokenName;
        }

        public String getTokenSymbol() {
            return tokenSymbol;
        }

        public void setTokenSymbol(String tokenSymbol) {
            this.tokenSymbol = tokenSymbol;
        }

        public String getTokenDecimal() {
            return tokenDecimal;
        }

        public void setTokenDecimal(String tokenDecimal) {
            this.tokenDecimal = tokenDecimal;
        }

        public String getSerialType() {
            return serialType;
        }

        public void setSerialType(String serialType) {
            this.serialType = serialType;
        }

        public String getInOrOut() {
            return inOrOut;
        }

        public void setInOrOut(String inOrOut) {
            this.inOrOut = inOrOut;
        }
    }
}
