use PizzaDB;

drop user 'dbtester';

create user 'dbtester' identified by 'CPSC4620';

grant all on PizzaDB.* to 'dbtester';