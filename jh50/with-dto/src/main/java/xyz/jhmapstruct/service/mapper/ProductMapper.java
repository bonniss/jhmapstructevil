package xyz.jhmapstruct.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import xyz.jhmapstruct.domain.Category;
import xyz.jhmapstruct.domain.Order;
import xyz.jhmapstruct.domain.Product;
import xyz.jhmapstruct.domain.Supplier;
import xyz.jhmapstruct.service.dto.CategoryDTO;
import xyz.jhmapstruct.service.dto.OrderDTO;
import xyz.jhmapstruct.service.dto.ProductDTO;
import xyz.jhmapstruct.service.dto.SupplierDTO;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryName")
    @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "supplierIdSet")
    ProductDTO toDto(Product s);

    @Mapping(target = "suppliers", ignore = true)
    @Mapping(target = "removeSuppliers", ignore = true)
    Product toEntity(ProductDTO productDTO);

    @Named("categoryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategoryDTO toDtoCategoryName(Category category);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);

    @Named("supplierId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SupplierDTO toDtoSupplierId(Supplier supplier);

    @Named("supplierIdSet")
    default Set<SupplierDTO> toDtoSupplierIdSet(Set<Supplier> supplier) {
        return supplier.stream().map(this::toDtoSupplierId).collect(Collectors.toSet());
    }
}
