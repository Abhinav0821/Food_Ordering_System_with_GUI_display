import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderSystemTest {

    @BeforeEach
    void setUp() {
        OrderSystem.loadUserData();

        OrderSystem.getAdmins().add(new Admin("Admin1", "A001", "adminpass"));
        OrderSystem.getCustomers().add(new Customer("Customer1", "C001", "9999999999", "custpass", "Regular"));
        OrderSystem.saveUserData(); // Save the test data to ensure it persists for testing
    }

    @Test
    void testInvalidAdminLogin() {
        assertFalse(OrderSystem.authenticateAdmin("A001", "wrongpass"), "Admin login should fail with incorrect password.");
        assertFalse(OrderSystem.authenticateAdmin("A002", "adminpas"), "Admin login should fail for non-existent Staff ID.");
    }

    @Test
    void testInvalidCustomerLogin() {
        assertFalse(OrderSystem.authenticateCustomer("C001", "wrongpass"), "Customer login should fail with incorrect password.");
        assertFalse(OrderSystem.authenticateCustomer("C002", "custpass"), "Customer login should fail for non-existent Roll Number.");
    }

    @Test
    void testValidAdminLogin() {
        assertTrue(OrderSystem.authenticateAdmin("A001", "adminpas"), "Admin login should succeed with correct credentials.");
    }

    @Test
    void testValidCustomerLogin() {
        assertTrue(OrderSystem.authenticateCustomer("C001", "custpass"), "Customer login should succeed with correct credentials.");
    }
}
