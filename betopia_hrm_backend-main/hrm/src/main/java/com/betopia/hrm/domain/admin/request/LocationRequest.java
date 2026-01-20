package com.betopia.hrm.domain.admin.request;

import jakarta.persistence.Column;

public record LocationRequest(
         Integer countryId ,
         Integer parentId ,
         String name,
         String type,
         String geoHash,
         Boolean status
) {
}
