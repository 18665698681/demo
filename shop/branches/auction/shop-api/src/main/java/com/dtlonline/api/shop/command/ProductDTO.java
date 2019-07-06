package com.dtlonline.api.shop.command;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.alpha.core.base.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ProductDTO extends BaseObject {
    /**
     * 品类ID
     */
    private Long categoryId;
    /**
     * 规格筛选
     */
    private List<StandardRecordDTO> standardList = new ArrayList<>();
    /**
     * 1-现货品类 2-物流品类 3-仓储品类
     */
    private Integer type;
    /**
     * 搜索条件
     */
    private String keywords;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区
     */
    private String area;

    private Set<String> txnIdList;

    private Set<Long> parentIdSet;

    /**
     * 获取MyBatis分页请求对象
     */
    public <E> Page<E> getMyBatisPage(){
        return new Page<>(this.current,this.size);
    }
    private Integer current;

    private Integer size;
}
