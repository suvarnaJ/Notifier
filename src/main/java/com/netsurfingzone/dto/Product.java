package com.netsurfingzone.dto;
    
        import java.math.BigDecimal;
        import java.math.BigInteger;
        import java.util.ArrayList;
        import java.util.Iterator;
        import java.util.LinkedList;
        import java.util.List;
        import java.util.Map;
        import java.util.stream.Collectors;
public class Product {

        public BigInteger branchCode;
        public BigInteger prdId;
        public String accountCode;
        public BigDecimal actualBalance;
        public BigDecimal sumActBal;
        public BigInteger countOfAccts;

        public Product() {
        }

        public Product(BigInteger branchCode, BigInteger prdId, String accountCode, BigDecimal actualBalance) {
            this.branchCode = branchCode;
            this.prdId = prdId;
            this.accountCode = accountCode;
            this.actualBalance = actualBalance;
        }

        public BigInteger getCountOfAccts() {
            return countOfAccts;
        }

        public void setCountOfAccts(BigInteger countOfAccts) {
            this.countOfAccts = countOfAccts;
        }

        public BigDecimal getSumActBal() {
            return sumActBal;
        }

        public void setSumActBal(BigDecimal sumActBal) {
            this.sumActBal = sumActBal;
        }

        public BigInteger getBranchCode() {
            return branchCode;
        }

        public void setBranchCode(BigInteger branchCode) {
            this.branchCode = branchCode;
        }

        public BigInteger getPrdId() {
            return prdId;
        }

        public void setPrdId(BigInteger prdId) {
            this.prdId = prdId;
        }

        public String getAccountCode() {
            return accountCode;
        }

        public void setAccountCode(String accountCode) {
            this.accountCode = accountCode;
        }

        public BigDecimal getActualBalance() {
            return actualBalance;
        }

        public void setActualBalance(BigDecimal actualBalance) {
            this.actualBalance = actualBalance;
        }

        @Override
        public String toString() {
            return "Product{" + "branchCode:" + branchCode + ", prdId:" + prdId + ", accountCode:" + accountCode + ", actualBalance:" + actualBalance + ", sumActBal:" + sumActBal + ", countOfAccts:" + countOfAccts + '}';
        }

        public static void main(String[] args) {
            List<Product> al = new ArrayList<Product>();
            System.out.println(al);
            al.add(new Product(new BigInteger("01"), new BigInteger("11"), "001", new BigDecimal("10")));
            al.add(new Product(new BigInteger("01"), new BigInteger("11"), "002", new BigDecimal("10")));
            al.add(new Product(new BigInteger("01"), new BigInteger("12"), "003", new BigDecimal("10")));
            al.add(new Product(new BigInteger("01"), new BigInteger("12"), "004", new BigDecimal("10")));
            al.add(new Product(new BigInteger("01"), new BigInteger("12"), "005", new BigDecimal("10")));
            al.add(new Product(new BigInteger("01"), new BigInteger("13"), "006", new BigDecimal("10")));
            al.add(new Product(new BigInteger("02"), new BigInteger("11"), "007", new BigDecimal("10")));
            al.add(new Product(new BigInteger("02"), new BigInteger("11"), "008", new BigDecimal("10")));
            al.add(new Product(new BigInteger("02"), new BigInteger("12"), "009", new BigDecimal("10")));
            al.add(new Product(new BigInteger("02"), new BigInteger("12"), "010", new BigDecimal("10")));
            al.add(new Product(new BigInteger("02"), new BigInteger("12"), "011", new BigDecimal("10")));
            al.add(new Product(new BigInteger("02"), new BigInteger("13"), "012", new BigDecimal("10")));
            //Map<BigInteger, Long> counting = al.stream().collect(Collectors.groupingBy(Product::getBranchCode, Collectors.counting()));
            // System.out.println(counting);

            //group by branch code
            Map<BigInteger, List<Product>> groupByBrCd = al.stream().collect(Collectors.groupingBy(Product::getBranchCode, Collectors.toList()));
            System.out.println("\n\n\n" + groupByBrCd);

            Map<BigInteger, List<Product>> groupByPrId = null;
            // Create a final List to show for output containing one element of each group
            List<Product> finalOutputList = new LinkedList<Product>();
            Product newPrd = null;
            // Iterate over resultant  Map Of List
            Iterator<BigInteger> brItr = groupByBrCd.keySet().iterator();
            Iterator<BigInteger> prdidItr = null;



            BigInteger brCode = null;
            BigInteger prdId = null;

            Map<BigInteger, List<Product>> tempMap = null;
            List<Product> accListPerBr = null;
            List<Product> accListPerBrPerPrd = null;

            Product tempPrd = null;
            Double sum = null;
            while (brItr.hasNext()) {
                brCode = brItr.next();
                //get  list per branch
                accListPerBr = groupByBrCd.get(brCode);

                // group by br wise product wise
                groupByPrId=accListPerBr.stream().collect(Collectors.groupingBy(Product::getPrdId, Collectors.toList()));

                System.out.println("====================");
                System.out.println(groupByPrId);

                prdidItr = groupByPrId.keySet().iterator();
                while(prdidItr.hasNext()){
                    prdId=prdidItr.next();
                    // get list per brcode+product code
                    accListPerBrPerPrd=groupByPrId.get(prdId);
                    newPrd = new Product();
                    // Extract zeroth element to put in Output List to represent this group
                    tempPrd = accListPerBrPerPrd.get(0);
                    newPrd.setBranchCode(tempPrd.getBranchCode());
                    newPrd.setPrdId(tempPrd.getPrdId());

                    //Set accCOunt by using size of list of our group
                    newPrd.setCountOfAccts(BigInteger.valueOf(accListPerBrPerPrd.size()));
                    //Sum actual balance of our  of list of our group 
                    sum = accListPerBrPerPrd.stream().filter(o -> o.getActualBalance() != null).mapToDouble(o -> o.getActualBalance().doubleValue()).sum();
                    newPrd.setSumActBal(BigDecimal.valueOf(sum));
                    // Add product element in final output list

                    finalOutputList.add(newPrd);

                }

            }

            System.out.println("+++++++++++++++++++++++");
            System.out.println(finalOutputList);

        }
    }

