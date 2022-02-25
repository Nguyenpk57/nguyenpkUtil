package jUnit;

import org.apache.commons.lang3.StringUtils;
import org.junit.*;

public class RequestBankNationDecoderTest {

    public RequestBankNationDecoderTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of decode method, of class RequestBankNationDecoder.
     */
    @Test
    public void testDecode() {
        System.out.println("testDecode");

        String functionCode = "300000";
        String operation = "00";
        String phoneNumber = StringUtils.rightPad("51999888777", 20, " ");
        String idSession = "d34f869a-e8b8-4999-bbd6-82f775030c2f";
        String iso8583 = functionCode + operation + phoneNumber + idSession;
        Assert.assertEquals(iso8583, "3000000000000000051999888777d34f869a-e8b8-4999-bbd6-82f775030c2f");

    }

}