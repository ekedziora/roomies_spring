package pl.kedziora.emilek.roomies.database.management;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class DatabaseManagement {

	public static void main(String[] args) {
		schemaExport();
	}

	private static void schemaExport() {
		Configuration configuration = new Configuration();
		configuration = configuration.configure("hibernate.cfg.xml");

		SchemaExport export = new SchemaExport(configuration);
		export.create(true, true);
	}

}
