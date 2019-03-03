/**
 * Project Name:Cloud. Package Name:com.trisun.vicinity.my.order.activity.help. Date:2016/8/29
 * 10:14. Copyright (c) 2016, 广东友门鹿网络科技有限公司.
 */

package com.chunlangjiu.app.util;


import java.util.ArrayList;
import java.util.List;

/**
 * .ClassName: ListPaging(分页的工具类) date: 2016/8/29 10:14
 *
 * @author wushf
 */
public class PageUtils<T> {
    private int totalNum = 0;
    private String keyword = "";
    private int totalPage = 1;
    private int pageSize = 10;
    private int tempPage = 1; // 记载当前加载的是第几页
    private int page = 1;
    private List<T> list;

    /**
     * 获取list某个下标的中的值
     * 
     * @param index 下标
     * @return
     */
    public T get(int index) {
        if (list != null && index < list.size() && index >= 0) {
            return list.get(index);
        } else {
            return null;
        }

    }

    /**
     * 获取list某个下标的中的值
     *
     * @return
     */
    public void add(T t) {
        if (list != null) {
            list.add(t);
        } else {
            list = new ArrayList<>();
            list.add(t);
        }
    }

    /**
     * 删除list中某一个下标
     * 
     * @param index
     */
    public void remove(int index) {
        list.remove(index);
    }

    public void remove(T t) {
        list.remove(t);
    }


    /**
     * 分页总数据获取集合大小
     */
    public int getSize() {
        return list == null ? 0 : list.size();
    }

    public List<T> getList() {
        return list;
    }

    public boolean isEmpty(){
        return list!=null && list.size()>0?false:true;
    }

    public void setList(List<T> list) {
        this.list = list;
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTempPage() {
        return tempPage;
    }

    public void setTempPage(int tempPage) {
        this.tempPage = tempPage;
    }

    /**
     * 设置请求页码在当前页面下+1 待请求成功后 在设置 page = tempPage.
     * 
     * @return 下一页页码值
     */
    public int nextPage() {
        tempPage = page + 1;
        return tempPage;
    }

    /**
     * 设置当前请求的页面为第一页.
     * 
     * @return 第一页页码值
     */
    public int firstPage() {
        tempPage = 1;
        return tempPage;
    }

    /**
     * 订单列表加载成功，且page等于tempPage
     * 
     * @param tempList
     */
    public void loadListSuccess(List tempList) {
        if (tempPage == 1) {
            page = 1;
            // 当传值的集合为空的时候充初始化
            list = tempList == null ? new ArrayList<>() : tempList;
        } else if (list != null && tempPage > 1 && tempList != null && tempList.size() > 0) {
            page = tempPage;
            list.addAll(tempList);
        }
    }

    /**
     * 重置列表数据
     */
    public void resetListData() {
        totalNum = 0;
        keyword = "";
        totalPage = 1;
        tempPage = 1; // 记载当前加载的是第几页
        page = 1;
        if (list != null) {
            list.clear();
        }
    }

    /**
     * 判断是否有下一页.
     * 
     * @return 是否有下一页的布尔值
     */
    public boolean isNextPage() {
        boolean isNextPage = false;
        if (totalPage > 1 && page < totalPage) {
            isNextPage = true;
        }
        return isNextPage;
    }
}
