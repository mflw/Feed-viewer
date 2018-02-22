import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UnitTest extends TestCase {

    private Parser p;
    Offer testOffer = new Offer(12686, true, "(Л) Галастоп д/лечения ложной беременности и подавления лактации у сук 7мл*60", new Category(502), 913);

    @Before
    public void setUp() {
        p = new Parser("C:/Users/mzubkov/Documents/java projects/Tests/feeds/XML4GoodsRu.xml");
        p.getOffers();
    }

    @Test
    public void findOfferTest() {
        Assert.assertEquals(testOffer, p.findOffer(12686));
    }

    @Test
    public void findOfferFailedTest() {
        Assert.assertNull(p.findOffer(77777));
    }
}