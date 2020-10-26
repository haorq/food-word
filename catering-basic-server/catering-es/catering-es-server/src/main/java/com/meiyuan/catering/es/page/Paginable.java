package com.meiyuan.catering.es.page;

/**
 * 分页实体
 * @author zhoudong
 *
 */
public interface Paginable {
		/**
		 * 总记录数
		 *
		 * @author: wxf
		 * @date: 2020/6/23 15:47
		 * @return: {@link int}
		 * @version 1.1.0
		 **/
		 int getTotalCount();

		 /**
		 * 总页数
		 *
		 * @author: wxf
		 * @date: 2020/6/23 15:47
		 * @return: {@link int}
		 * @version 1.1.0
		 **/
		 int getTotalPage();

		 /**
		 * 每页记录数
		 *
		 * @author: wxf
		 * @date: 2020/6/23 15:47
		 * @return: {@link int}
		 * @version 1.1.0
		 **/
		 int getPageSize();

		/**
		 * 当前页号
		 *
		 * @author: wxf
		 * @date: 2020/6/23 15:47
		 * @return: {@link int}
		 * @version 1.1.0
		 **/
		 int getPageNo();

		/**
		 * 是否第一页
		 *
		 * @author: wxf
		 * @date: 2020/6/23 15:48
		 * @return: {@link boolean}
		 * @version 1.1.0
		 **/
		 boolean isFirstPage();
		/**
		 * 是否最后一页
		 *
		 * @author: wxf
		 * @date: 2020/6/23 15:48
		 * @return: {@link boolean}
		 * @version 1.1.0
		 **/
		 boolean isLastPage();

		/**
		 * 返回下页的页号
		 *
		 * @author: wxf
		 * @date: 2020/6/23 15:49
		 * @return: {@link int}
		 * @version 1.1.0
		 **/
		 int getNextPage();

		/**
		 * 返回上页的页号
		 *
		 * @author: wxf
		 * @date: 2020/6/23 15:49
		 * @return: {@link int}
		 * @version 1.1.0
		 **/
		 int getPrePage();
	}
