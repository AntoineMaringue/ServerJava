/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author antoi_000
 */
public class TestConnectionBase extends junit.framework.TestCase{
    
    class Items
    {
        private int id;
        private String label;

        private Items(int id, String label) {
           this.id = id;
           this.label = label;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
        
        
    }
    
    List<Items> objects_list;
    
    public TestConnectionBase() 
    {
        objects_list = new ArrayList<Items>()
        {{
                    add(new Items(1,"Test"));
                    add(new Items(2,"Test"));
                    add(new Items(3,"Test"));
        }};
        
        objects_list.forEach(
                o -> System.out.println(o.getId() + o.getLabel())
        //System.out::println
        );
        
        
        
        
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
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}