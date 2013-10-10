package com.kspichale.firebrand;

import static org.fest.assertions.Assertions.assertThat;
import static org.firebrandocm.dao.cql.QueryBuilder.allColumns;
import static org.firebrandocm.dao.cql.QueryBuilder.eq;
import static org.firebrandocm.dao.cql.QueryBuilder.from;
import static org.firebrandocm.dao.cql.QueryBuilder.select;
import static org.firebrandocm.dao.cql.QueryBuilder.where;

import java.util.ArrayList;
import java.util.List;

import org.firebrandocm.dao.PersistenceFactory;
import org.firebrandocm.dao.Query;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:repository-context.xml")
public class FirebrandIT {

	@Autowired
	private PersistenceFactory persistenceFactory;

	@Test
	public void createContext() {

		final User user = persistenceFactory.getInstance(User.class);

		user.setName("user1");
		user.setPassword("secure");

		final List<Address> addresses = new ArrayList<Address>();
		final Address address = persistenceFactory.getInstance(Address.class);
		address.setStreet("street");
		persistenceFactory.persist(address);
		addresses.add(address);
		user.setAddresses(addresses);

		persistenceFactory.persist(user);

		final User loadedUser = persistenceFactory.get(User.class, user.getKey());
		assertThat(loadedUser.getName()).isEqualTo(user.getName());
		assertThat(loadedUser.getPassword()).isEqualTo(user.getPassword());
		assertThat(loadedUser.getAddresses()).isEqualTo(user.getAddresses());

		List<User> resultList = persistenceFactory.getResultList(User.class,
				Query.get(select(allColumns(), from(User.class), where(eq("name", "user1")))));

		assertThat(resultList).isNotEmpty();
	}
}
