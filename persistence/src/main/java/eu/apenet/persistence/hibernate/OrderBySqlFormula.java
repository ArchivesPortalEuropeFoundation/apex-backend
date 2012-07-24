package eu.apenet.persistence.hibernate;
 
import org.hibernate.criterion.Order;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;

public class OrderBySqlFormula extends Order {
    private String sqlFormula;
 
    protected OrderBySqlFormula(String sqlFormula) {
        super(sqlFormula, true);
        this.sqlFormula = sqlFormula;
    }
 
    public String toString() {
        return sqlFormula;
    }
 
    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return sqlFormula;
    }

    public static Order sqlFormula(String sqlFormula) {
        return new OrderBySqlFormula(sqlFormula);
    }
}