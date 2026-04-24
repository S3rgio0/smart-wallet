import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SmartWalletTest {

    private SmartWallet myWallet;

    @BeforeEach
    void initWallet() {
        myWallet = new SmartWallet();
    }

    // 1. Camino Feliz: Depósito normal
    @Test
    void shouldDepositNormallyWithoutCashback() {
        boolean isSuccess = myWallet.deposit(80);
        assertTrue(isSuccess);
        assertEquals(80.0, myWallet.getBalance());
    }

    // 2. Camino Feliz y Límite: Aplica 1% de cashback
    @Test
    void shouldApplyCashbackForLargeDeposits() {
        // Deposita 150 -> Cashback es 1.5 -> Total 151.5
        boolean isSuccess = myWallet.deposit(150);
        assertTrue(isSuccess);
        assertEquals(151.5, myWallet.getBalance());
    }

    // 3. Casos de error: Valores inválidos al depositar
    @Test
    void shouldRejectZeroOrNegativeDeposits() {
        assertFalse(myWallet.deposit(0));
        assertFalse(myWallet.deposit(-25.5));
        assertEquals(0.0, myWallet.getBalance());
    }

    // 4. Límite: No pasar los 5000 en Standard
    @Test
    void shouldPreventStandardUserFromExceedingLimit() {
        myWallet.deposit(100);
        // Intentamos sumar algo que vuele el límite (4900 + 49 de cashback = 4949. Saldo total: 5049)
        boolean isSuccess = myWallet.deposit(4900);
        
        assertFalse(isSuccess, "El sistema no debe permitir exceder los $5000");
    }

    // 5. Camino Feliz: Retiro normal
    @Test
    void shouldWithdrawSuccessfully() {
        myWallet.deposit(200); // Saldo: 202.0 por el cashback
        boolean isSuccess = myWallet.withdraw(52.0);
        
        assertTrue(isSuccess);
        assertEquals(150.0, myWallet.getBalance());
    }

    // 6. Casos de error: Valores inválidos al retirar
    @Test
    void shouldRejectInvalidWithdrawalAmounts() {
        myWallet.deposit(100);
        assertFalse(myWallet.withdraw(0));
        assertFalse(myWallet.withdraw(-100));
    }

    // 7. Casos de error: Sin fondos suficientes
    @Test
    void shouldDeclineWithdrawalWhenFundsAreInsufficient() {
        myWallet.deposit(50);
        boolean isSuccess = myWallet.withdraw(100);
        
        assertFalse(isSuccess);
        assertEquals(50.0, myWallet.getBalance()); // Saldo intacto
    }

    // 8. Límite: Inactivar cuenta al vaciarse
    @Test
    void shouldDeactivateAccountUponZeroBalance() {
        myWallet.deposit(100); // Saldo es 100
        myWallet.withdraw(100); // Retira exactamente 100
        
        assertEquals(0.0, myWallet.getBalance());
        assertFalse(myWallet.isActive(), "La cuenta debió cambiar a estado inactivo");
    }
}