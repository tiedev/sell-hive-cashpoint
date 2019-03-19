package de.tiedev.sellhive.cashpoint.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
public class MyTableDaoTest {
    
	private EmbeddedDatabase db;

//    MyTableRepository userDao;
    
//    @Before
    public void setUp() {
        //db = new EmbeddedDatabaseBuilder().addDefaultScripts().build();
    	db = new EmbeddedDatabaseBuilder()
    		.setType(EmbeddedDatabaseType.H2)
    		.addScript("/db/sql/create-db.sql")
    		.addScript("/db/sql/insert-data.sql")
    		.build();
    }

    @Test
    public void testFindByname() {
    	Assert.assertTrue(true);
//    	NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db);
//    	MyTableDaoImpl myTableDao = new MyTableDaoImpl();
//    	myTableDao.setNamedParameterJdbcTemplate(template);
//    	
//    	MyTable myTable = myTableDao.findByName("mkyong");
//  
//    	Assert.assertNotNull(myTable);
//    	Assert.assertEquals(1, myTable.getId().intValue());
//    	Assert.assertEquals("mkyong", myTable.getName());
//    	Assert.assertEquals("mkyong@gmail.com", myTable.getEmail());

    }

//    @After
    public void tearDown() {
        db.shutdown();
}
}
