package com.zeroone.star.storemanagement.convertor;
import com.zeroone.star.project.dto.j2.store.OtherInListDTO;
import com.zeroone.star.storemanagement.entity.EntryDO;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
/**
 * EntryDO 和 OtherInListDTO 之间的转换器
 * MapStruct会在编译时自动生成这个接口的实现类
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EntryConverter {

    /**
     * DO -> DTO
     * LocalDateTime -> Long (时间戳)
     */
    @Mapping(source = "time", target = "time", qualifiedByName = "localDateTimeToTimestamp")
    @Mapping(source = "total", target = "total", qualifiedByName = "bigDecimalToDouble")
    @Mapping(source = "cost", target = "cost", qualifiedByName = "bigDecimalToDouble")
    OtherInListDTO toDTO(EntryDO entryDO);

    List<OtherInListDTO> toDTOList(List<EntryDO> entryDOList);

    /**
     * DTO -> DO
     * Long (时间戳) -> LocalDateTime
     */
    @Mapping(source = "time", target = "time", qualifiedByName = "timestampToLocalDateTime")
    @Mapping(source = "total", target = "total", qualifiedByName = "doubleToBigDecimal")
    @Mapping(source = "cost", target = "cost", qualifiedByName = "doubleToBigDecimal")
    EntryDO toDO(OtherInListDTO dto);

    /**
     * LocalDateTime -> Long (Unix 时间戳)
     */
    @Named("localDateTimeToTimestamp")
    default Long localDateTimeToTimestamp(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.atZone(java.time.ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
    }

    /**
     * Long (Unix 时间戳) -> LocalDateTime
     */
    @Named("timestampToLocalDateTime")
    default LocalDateTime timestampToLocalDateTime(Long timestamp) {
        if (timestamp == null || timestamp == 0) {
            return null;
        }
        return LocalDateTime.ofInstant(
                java.time.Instant.ofEpochSecond(timestamp),
                java.time.ZoneId.systemDefault()
        );
    }

    @Named("bigDecimalToDouble")
    default Double bigDecimalToDouble(BigDecimal value) {
        return value == null ? null : value.doubleValue();
    }

    @Named("doubleToBigDecimal")
    default BigDecimal doubleToBigDecimal(Double value) {
        return value == null ? null : BigDecimal.valueOf(value);
    }
}