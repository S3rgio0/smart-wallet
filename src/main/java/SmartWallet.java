public class SmartWallet {
    private double currentBalance;
    private boolean accountActive;
    private String membershipLevel;


    private static final double MAX_STANDARD_LIMIT = 5000.0;

    public SmartWallet() {
        this.currentBalance = 0.0;
        this.accountActive = true;
        this.membershipLevel = "Standard";
    }

    public boolean deposit(double amountToDeposit) {
        if (amountToDeposit <= 0) {
            return false;
        }

        double extraCashback = calculateCashback(amountToDeposit);
        double finalAmount = amountToDeposit + extraCashback;

        // Validar límite para cuentas Standard
        if (membershipLevel.equals("Standard") && (currentBalance + finalAmount > MAX_STANDARD_LIMIT)) {
            return false;
        }

        this.currentBalance += finalAmount;
        return true;
    }

    private double calculateCashback(double amount) {
        return (amount > 100.0) ? (amount * 0.01) : 0.0;
    }

    public boolean withdraw(double amountToWithdraw) {
        if (amountToWithdraw <= 0.0 || amountToWithdraw > currentBalance) {
            return false;
        }

        this.currentBalance -= amountToWithdraw;

        if (this.currentBalance == 0.0) {
            this.accountActive = false;
        }

        return true;
    }

    public double getBalance() {
        return currentBalance;
    }

    public boolean isActive() {
        return accountActive;
    }
}