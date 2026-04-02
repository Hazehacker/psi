package com.zeroone.star.storemanagement.convertor;

import com.zeroone.star.project.dto.j2.store.OtherOutListDTO;
import com.zeroone.star.storemanagement.entity.ExtryDO;
import com.zeroone.star.storemanagement.entity.ExtryDO;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExtryConverter {
    /**
     * DO -> DTO
     * LocalDateTime -> Long (时间戳)
     */


    List<OtherOutListDTO> toDTOList(List<ExtryDO> extryDOList);

    /**
     * DTO -> DO
     * Long (时间戳) -> LocalDateTime
     */


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
