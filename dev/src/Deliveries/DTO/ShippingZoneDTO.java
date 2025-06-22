package Deliveries.DTO;

import Deliveries.Domain.ShippingZone;
import Deliveries.Domain.Site;

import java.util.List;

// This file defines the ShippingZoneDTO record, which is used to encapsulate shipping zone data.
public record ShippingZoneDTO(
        String shippingZoneID,
        String shippingZoneName,
        List<SiteDTO> sites

) {

}
