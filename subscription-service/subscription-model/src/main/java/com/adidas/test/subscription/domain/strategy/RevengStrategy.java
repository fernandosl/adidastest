package com.adidas.test.subscription.domain.strategy;

import java.sql.Types;
import org.hibernate.cfg.reveng.DelegatingReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.TableIdentifier;

public class RevengStrategy extends DelegatingReverseEngineeringStrategy {

  public RevengStrategy(
      ReverseEngineeringStrategy delegate) {
    super(delegate);
  }

  @Override
  public String columnToHibernateTypeName(TableIdentifier table, String columnName, int sqlType,
      int length, int precision, int scale, boolean nullable, boolean generatedIdentifier) {
    System.out.println("TABLE " + table.getName() + " columName " + columnName + " type " + sqlType
        + " length " + length + " precision " + precision + " scale " + scale);
    if (sqlType == Types.DECIMAL && scale == 0) {
      return "java.lang.Long";
    } else {
      return super
          .columnToHibernateTypeName(table, columnName, sqlType, length, precision, scale, nullable,
              generatedIdentifier);
    }
  }

  @Override
  public boolean excludeTable(TableIdentifier ti) {
    System.out.println("Processing Table " + ti.getName());
    return !(ti.getName().startsWith("SUBS_"));
  }
}
