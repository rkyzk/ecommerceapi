package com.restapi.ecommerce.repository;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.restapi.ecommerce.entity.Product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Return the number of results
	 * 
	 * @param categoryId
	 * @param keywords
	 * @param colors
	 * 
	 * @return number of results
	 */
	@Override
	public Long getTotalElements(String categoryId, List<String> keywords,
			String colors) {
        StringBuilder sql = new StringBuilder("SELECT COUNT (DISTINCT p.id) FROM products p "
        		+ "INNER JOIN product_detail pd ON p.id = pd.product_id "
        		+ "INNER JOIN product_color pc ON p.id = pc.product_id "
        		+ "WHERE p.deleted_at is null");
        if (!StringUtils.isEmpty(categoryId)) {
        	sql.append(" AND p.category_id = :categoryId");
        }
        if (keywords != null) {
            for (int i = 0; i < keywords.size(); i++) {
            	sql.append(" AND (LOWER(p.product_name) LIKE ?")
            		.append(i + 1).append(" OR LOWER(pd.value) LIKE ?")
            		.append(i + 1).append(")");
            }
        }
        if (colors != null) {
        	sql.append(" AND pc.color IN ").append(colors);
        }
        Query query = entityManager.createNativeQuery(sql.toString(), Long.class);
        if (!StringUtils.isEmpty(categoryId)) {
        	query.setParameter("categoryId", Integer.parseInt(categoryId));
        }
        if (keywords != null) {
            for (int i = 0; i < keywords.size(); i++) {
                query.setParameter(i + 1, "%" + keywords.get(i).toLowerCase() + "%");
            }
        }
        return (Long)query.getSingleResult();
	};

	/**
	 * Return product list (when not sorted by sales count)
	 * 
	 * @param pageNUmber
	 * @param pageSize
	 * @param sortBy
	 * @param sortOrder
	 * @param categoryId
	 * @param keywords
	 * @param colors
	 * 
	 * @return product list
	 */
	@Override
    public List<Product> getProducts(Integer pageNumber, Integer pageSize,
    		String sortBy, String sortOrder, String categoryId, List<String> keywords, String colors) {
        StringBuilder sql = new StringBuilder("SELECT DISTINCT p.* FROM products p "
        		+ "INNER JOIN product_detail pd ON p.id = pd.product_id "
        		+ "INNER JOIN product_color pc ON p.id = pc.product_id WHERE p.deleted_at is null");
        if (!StringUtils.isEmpty(categoryId)) {
        	sql.append(" AND p.category_id = :categoryId");
        }
        if (keywords != null) {
            for (int i = 0; i < keywords.size(); i++) {
            	sql.append(" AND (LOWER(p.product_name) LIKE ?")
            		.append(i + 1).append(" OR LOWER(pd.value) LIKE ?")
            		.append(i + 1).append(")");
            }
        }
        if (colors != null) {
        	sql.append(" AND pc.color IN ").append(colors);
        }
    	sql.append(" ORDER BY p.").append(sortBy)
    		.append(" OFFSET ")
    		.append(pageSize * pageNumber).append(" ROWS FETCH NEXT ")
    		.append(pageSize).append(" ROWS ONLY");
        Query query = entityManager.createNativeQuery(sql.toString(), Product.class);
        if (!StringUtils.isEmpty(categoryId)) {
        	query.setParameter("categoryId", Integer.parseInt(categoryId));
        }
        if (keywords != null) {
            for (int i = 0; i < keywords.size(); i++) {
                query.setParameter(i + 1, "%" + keywords.get(i).toLowerCase() + "%");
            }
        }
        return query.getResultList();
    }

	/**
	 * Return product list sorted by sales count
	 * 
	 * @param pageNUmber
	 * @param pageSize
	 * @param categoryId
	 * @param keywords
	 * @param colors
	 * 
	 * @return product list
	 */
	@Override
    public List<Product> getProductsSortBySalesCount(Integer pageNumber, Integer pageSize,
    		String categoryId, List<String> keywords, String colors) {
        StringBuilder sql = new StringBuilder("SELECT DISTINCT p.*, SUM(CASE WHEN "
        		+ "p.id = sc.product_id AND sc.date > CURRENT_DATE - 30 "
        		+ "THEN sc.quantity ELSE 0 END) AS qty FROM products p "
        		+ "INNER JOIN product_detail pd ON p.id = pd.product_id "
        		+ "INNER JOIN product_color pc ON p.id = pc.product_id "
        		+ "LEFT OUTER JOIN sales_count sc ON p.id = sc.product_id "
        		+ "WHERE p.deleted_at is null");
        if (!StringUtils.isEmpty(categoryId)) {
        	sql.append(" AND p.category_id = :categoryId");
        }
        if (keywords != null) {
            for (int i = 0; i < keywords.size(); i++) {
                sql.append(" AND (LOWER(p.product_name) LIKE ?")
                	.append(i + 1).append(" OR LOWER(pd.value) LIKE ?")
                    .append(i + 1).append(")");
            }
        }
        if (colors != null) {
        	sql.append(" AND pc.color IN ").append(colors);
        }
        sql.append(" GROUP BY p.id ORDER BY qty DESC, p.id ASC")
        	.append(" OFFSET ")
        	.append(pageSize * pageNumber).append(" ROWS FETCH NEXT ")
        	.append(pageSize).append(" ROWS ONLY");
        Query query = entityManager.createNativeQuery(sql.toString(), Product.class);
        if (!StringUtils.isEmpty(categoryId)) {
        	query.setParameter("categoryId", Integer.parseInt(categoryId));
        }
        if (keywords != null && !keywords.isEmpty()) {
            for (int i = 0; i < keywords.size(); i++) {
                query.setParameter(i + 1, "%" + keywords.get(i).toLowerCase() + "%");
            }
        }
        return query.getResultList();
    }
}
