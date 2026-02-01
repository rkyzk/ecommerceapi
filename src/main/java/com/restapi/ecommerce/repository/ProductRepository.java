package com.restapi.ecommerce.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.restapi.ecommerce.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
	/** 並べ替え・売れ行き順SQL共通部分 -------------------------------- **/
	String querySortBySalesCount = "SELECT p.*, SUM(CASE WHEN p.id = sc.product_id AND "
			+ "sc.date > CURRENT_DATE - 30 THEN sc.quantity ELSE 0 END) AS qty FROM products p "
			+ "LEFT OUTER JOIN sales_count sc ON p.id = sc.product_id ";

	/** 
	 * フィルターなし
	 * @param pageDetails
	 * @return
	 */
	Page<Product> findByDeletedAtIsNull(Pageable pageDetails);

	/** フィルターなし、売れ行き順 SQLクエリ -------------------------------- **/
	String query1 = querySortBySalesCount
			+ "WHERE p.deleted_at is null GROUP BY p.id ORDER BY qty DESC, p.id ASC";
	String countQuery1 = "SELECT COUNT(p.id) FROM products p WHERE p.deleted_at is null"; 

	/** 
	 * フィルターなし、売れ行き順
	 * @param pageDetails
	 * @return
	 */
	@Query(value = query1, countQuery = countQuery1, nativeQuery=true)
	Page<Product> findByDeletedAtIsNullSortBySalesCount(Pageable pageDetails);

	/**
	 * カテゴリー指定
	 * @param categoryId
	 * @param pageDetails
	 * @return
	 */
	Page<Product> findByCategoryCategoryIdAndDeletedAtIsNull(Long categoryId, Pageable pageDetails);

	/** カテゴリー指定、売れ行き順 SQLクエリ -------------------------------- **/
	String queryFilterByCategorySortBySalesCount = querySortBySalesCount
			+ "WHERE p.category_id = :categoryId AND p.deleted_at is null "
			+ "GROUP BY p.id ORDER BY qty DESC, p.id ASC";
    String countQueryFilterByCategory = "SELECT COUNT(p.id) FROM products p "
    		+ "WHERE p.category_id = :categoryId AND p.deleted_at is null";

	/**
	 * カテゴリー指定、売れ行き順
	 *
	 * @param categoryId
	 * @param pageDetails
	 * @return
	 */
	@Query(value = queryFilterByCategorySortBySalesCount,
		   countQuery = countQueryFilterByCategory,
		   nativeQuery=true)
	Page<Product> findByCategoryCategoryIdAndDeletedAtIsNullSortBySalesCount(Long categoryId, Pageable pageDetails);

	/** カテゴリー、色、キーワード指定 SQLクエリ -------------------------------- **/
	String query3 = "SELECT DISTINCT p.* FROM products p INNER JOIN product_detail pd ON "
			+ "p.id = pd.product_id INNER JOIN product_color pc ON p.id = pc.product_id WHERE p.category_id >= :categoryIdMin AND "
			+ "p.category_id <= :categoryIdMax AND p.deleted_at is null AND "
			+ "(LOWER(p.product_name) LIKE LOWER(CONCAT('%',:keyword,'%')) OR (pd.key = 'Description' AND "
			+ "LOWER(pd.value) LIKE LOWER(CONCAT('%',:keyword,'%')))) AND "
			+ "(LOWER(p.product_name) LIKE LOWER(CONCAT('%',:keyword2,'%')) OR (pd.key = 'Description' AND "
			+ "LOWER(pd.value) LIKE LOWER(CONCAT('%',:keyword2,'%')))) AND "
			+ "(LOWER(p.product_name) LIKE LOWER(CONCAT('%',:keyword3,'%')) OR (pd.key = 'Decription' AND "
			+ "LOWER(pd.value) LIKE LOWER(CONCAT('%',:keyword3,'%')))) AND "
			+ "(pc.color = :colorId1 OR pc.color = :colorId2 OR pc.color = :colorId3 OR "
			+ "pc.color = :colorId4 OR pc.color = :colorId5 OR pc.color = :colorId6 OR "
			+ "pc.color = :colorId7 OR pc.color = :colorId8)";
	String countQuery3 = "SELECT COUNT(DISTINCT p.id) FROM products p INNER JOIN product_detail pd ON "
			+ "p.id = pd.product_id INNER JOIN product_color pc ON p.id = pc.product_id "
		    + "WHERE p.category_id >= :categoryIdMin AND p.category_id <= :categoryIdMax AND p.deleted_at is null AND "
			+ "(LOWER(p.product_name) LIKE LOWER(CONCAT('%',:keyword,'%')) OR (pd.key = 'Description' AND "
			+ "LOWER(pd.value) LIKE LOWER(CONCAT('%',:keyword,'%')))) AND "
			+ "(LOWER(p.product_name) LIKE LOWER(CONCAT('%',:keyword2,'%')) OR (pd.key = 'Description' AND "
			+ "LOWER(pd.value) LIKE LOWER(CONCAT('%',:keyword2,'%')))) AND "
			+ "(LOWER(p.product_name) LIKE LOWER(CONCAT('%',:keyword3,'%')) OR (pd.key = 'Decription' AND "
			+ "LOWER(pd.value) LIKE LOWER(CONCAT('%',:keyword3,'%')))) AND "
			+ "(pc.color = :colorId1 OR pc.color = :colorId2 OR pc.color = :colorId3 OR "
			+ "pc.color = :colorId4 OR pc.color = :colorId5 OR pc.color = :colorId6 OR "
			+ "pc.color = :colorId7 OR pc.color = :colorId8)";

	/**
	 * カテゴリー、色、キーワード指定
	 *
	 * @param keyword
	 * @param keyword2
	 * @param keyword3
	 * @param categoryIdMin
	 * @param categoryIdMax
	 * @param colorId1
	 * @param colorId2
	 * @param colorId3
	 * @param colorId4
	 * @param colorId5
	 * @param colorId6
	 * @param colorId7
	 * @param colorId8
	 * @param pageDetails
	 * @return
	 */ 
	@Query(value = query3,
		   countQuery = countQuery3,
		   nativeQuery=true)
	Page<Product> findProductsByKeywordsAndCategoryAndColors(
			String keyword, String keyword2, String keyword3, Long categoryIdMin, Long categoryIdMax,
			Long colorId1, Long colorId2, Long colorId3, Long colorId4, Long colorId5, Long colorId6,
			Long colorId7, Long colorId8, Pageable pageDetails);

	/** カテゴリー、色、キーワード指定、売れ行き順 SQLクエリ -------------------------------- **/
	String query4 = querySortBySalesCount + "INNER JOIN product_detail pd ON p.id = pd.product_id "
			+ "INNER JOIN product_color pc ON p.id = pc.product_id "
			+ "WHERE p.category_id >= :categoryIdMin AND p.category_id <= :categoryIdMax AND "
			+ "p.deleted_at is null AND "
			+ "(LOWER(p.product_name) LIKE LOWER(CONCAT('%',:keyword,'%')) OR (pd.key = 'Description' AND "
			+ "LOWER(pd.value) LIKE LOWER(CONCAT('%',:keyword,'%')))) AND "
			+ "(LOWER(p.product_name) LIKE LOWER(CONCAT('%',:keyword2,'%')) OR (pd.key = 'Description' AND "
			+ "LOWER(pd.value) LIKE LOWER(CONCAT('%',:keyword2,'%')))) AND "
			+ "(LOWER(p.product_name) LIKE LOWER(CONCAT('%',:keyword3,'%')) OR (pd.key = 'Decription' AND "
			+ "LOWER(pd.value) LIKE LOWER(CONCAT('%',:keyword3,'%')))) AND "
			+ "(pc.color = :colorId1 OR pc.color = :colorId2 OR pc.color = :colorId3 OR "
			+ "pc.color = :colorId4 OR pc.color = :colorId5 OR pc.color = :colorId6 OR "
			+ "pc.color = :colorId7 OR pc.color = :colorId8) GROUP BY p.id ORDER BY qty DESC, p.id ASC";

	String countQuery4 = "SELECT COUNT(DISTINCT p.id) FROM products p "
			+ "INNER JOIN product_detail pd ON p.id = pd.product_id "
			+ "INNER JOIN product_color pc ON p.id = pc.product_id "
			+ "WHERE p.category_id >= :categoryIdMin AND p.category_id <= :categoryIdMax AND "
			+ "p.deleted_at is null AND "
			+ "(LOWER(p.product_name) LIKE LOWER(CONCAT('%',:keyword,'%')) OR (pd.key = 'Description' AND "
			+ "LOWER(pd.value) LIKE LOWER(CONCAT('%',:keyword,'%')))) AND "
			+ "(LOWER(p.product_name) LIKE LOWER(CONCAT('%',:keyword2,'%')) OR (pd.key = 'Description' AND "
			+ "LOWER(pd.value) LIKE LOWER(CONCAT('%',:keyword2,'%')))) AND "
			+ "(LOWER(p.product_name) LIKE LOWER(CONCAT('%',:keyword3,'%')) OR (pd.key = 'Decription' AND "
			+ "LOWER(pd.value) LIKE LOWER(CONCAT('%',:keyword3,'%')))) AND "
			+ "(pc.color = :colorId1 OR pc.color = :colorId2 OR pc.color = :colorId3 OR "
			+ "pc.color = :colorId4 OR pc.color = :colorId5 OR pc.color = :colorId6 OR "
			+ "pc.color = :colorId7 OR pc.color = :colorId8)";

	/**
	 * カテゴリー、色、キーワード指定、売れ行き順
	 *
	 * @param keyword
	 * @param keyword2
	 * @param keyword3
	 * @param categoryIdMin
	 * @param categoryIdMax
	 * @param colorId1
	 * @param colorId2
	 * @param colorId3
	 * @param colorId4
	 * @param colorId5
	 * @param colorId6
	 * @param colorId7
	 * @param colorId8
	 * @param pageDetails
	 * @return
	 */
	@Query(value = query4, countQuery = countQuery4, nativeQuery=true)
	Page<Product> findProductsByKeywordsAndCategoryAndColorsSortBySalesCount(
			String keyword, String keyword2, String keyword3, Long categoryIdMin, Long categoryIdMax, Long colorId1,
			Long colorId2, Long colorId3, Long colorId4, Long colorId5, Long colorId6,
			Long colorId7, Long colorId8, Pageable pageDetails);

	/**
	 * 商品を取得
	 * 
	 * @param productName
	 * @return
	 */
	Product findByProductName(String productName);

	// 未使用
	List<Product> findByFeaturedIsTrue();

	@Modifying
	@Query(value="UPDATE products p SET p.quantity = ?2 "
			+ "where p.id = ?1", nativeQuery=true)
	void updateProductQuantity(Long id, Integer quantity);
}