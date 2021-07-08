package com.adidas.test.subscription.webcontroller.controller.rest;

import com.adidas.test.pubservice.common.error.ApplicationException;
import com.adidas.test.pubservice.common.web.controller.BaseRestController;
import com.adidas.test.pubservice.common.web.util.WebControllerUtils;
import com.adidas.test.subscription.domain.dto.SubscriptionDTO;
import com.adidas.test.subscription.domain.entity.SubscriptionEntity;
import com.adidas.test.subscription.domain.service.SubscriptionService;
import com.adidas.test.subscription.webcontroller.util.ApplicationConstants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApplicationConstants.API_VERSION + "/subscription")
public class SubscriptionRestController extends BaseRestController {

  @Autowired
  SubscriptionService subscriptionService;

  @PreAuthorize("hasRole('ROLE_PUB_SUBSCRIBE')")
  @PostMapping(
      value = "/create",
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @ApiOperation(value = "createSubscription", notes = "Creates a new subscription")
  @ResponseStatus(value = HttpStatus.OK)
  @ResponseBody
  public ResponseEntity<SubscriptionDTO> createSubscription(
      @ApiParam(value = "Object with the subscription details to be created", required = true) @Valid @RequestBody(required = true) final SubscriptionDTO subscriptionDTO
  ) {
    ModelMapper modelMapper = new ModelMapper();
    SubscriptionEntity subscriptionEntity = modelMapper
        .map(subscriptionDTO, SubscriptionEntity.class);
    subscriptionEntity.setId(null);

    Optional<SubscriptionEntity> subscriptionEntityCreated = subscriptionService
        .createSubscription(subscriptionEntity);

    if (subscriptionEntityCreated.isPresent()) {
      SubscriptionDTO subscriptionDTOCreated = modelMapper
          .map(subscriptionEntity, SubscriptionDTO.class);
      return new ResponseEntity<>(subscriptionDTOCreated, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
  }

  @PreAuthorize("hasRole('ROLE_PUB_SUBSCRIBE')")
  @GetMapping(
      value = "/cancel/{subscriptionId}",
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @ApiOperation(value = "cancelSubscriptionById", notes = "Cancels subscription by id of subscription")
  @ResponseStatus(value = HttpStatus.OK)
  @ResponseBody
  public ResponseEntity<String> cancelSubscriptionById(
      @ApiParam(value = "Id of subscription to cancel", required = true) @PathVariable("subscriptionId") final String subscriptionId)
      throws ApplicationException {
    if (StringUtils.isNumeric(subscriptionId)) {
      Boolean bResult = subscriptionService
          .cancelSubscriptionById(Long.parseLong(subscriptionId));

      if (bResult != null && Boolean.TRUE == bResult) {
        return new ResponseEntity<>(Boolean.TRUE.toString(), HttpStatus.OK);
      } else {
        return new ResponseEntity<>(Boolean.FALSE.toString(), HttpStatus.OK);
      }
    } else {
      throw new ApplicationException("Error in parameters");
    }
  }

  /**
   * Gets one subscription by its id
   *
   * @param subscriptionId
   * @return
   * @throws ApplicationException
   */
  @PreAuthorize("hasRole('ROLE_PUB_SUBSCRIBE')")
  @GetMapping(
      value = "/get/{subscriptionId}",
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @ApiOperation(value = "getSubscriptionById", notes = "Gets subscription by id of subscription")
  @ResponseStatus(value = HttpStatus.OK)
  @ResponseBody
  public ResponseEntity<SubscriptionDTO> getSubscriptionById(
      @ApiParam(value = "Id of created subscription", required = true) @PathVariable("subscriptionId") final String subscriptionId)
      throws ApplicationException {
    if (StringUtils.isNumeric(subscriptionId)) {
      Optional<SubscriptionEntity> subscriptionEntity = subscriptionService
          .getSubscriptionById(Long.parseLong(subscriptionId));
      if (subscriptionEntity.isPresent()) {
        ModelMapper modelMapper = new ModelMapper();
        SubscriptionDTO subscriptionDTO = modelMapper
            .map(subscriptionEntity.get(), SubscriptionDTO.class);
        return new ResponseEntity<>(subscriptionDTO, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
    } else {
      throw new ApplicationException("Error in parameters");
    }
  }

  /**
   * Gets a list of subscriptions, returns requested page and total number of elements in
   * X-Total-Count header
   *
   * @param _page
   * @param _rows
   * @param _sort
   * @param _direction
   * @param subscriptionDTO
   * @return
   */
  @PreAuthorize("hasRole('ROLE_PUB_SUBSCRIBE')")
  @PostMapping(
      value = "/list",
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @ApiOperation(value = "listSubscriptions", notes = "List requested subscriptions, result is paginated. Total query count is returned in X-Total-Count header")
  @ResponseStatus(value = HttpStatus.OK)
  @ResponseBody
  public ResponseEntity<List<SubscriptionDTO>> listSubscriptions(
      @ApiParam(value = "Number of page to recover", required = false) @RequestParam(required = false) String _page,
      @ApiParam(value = "Number of registers per page", required = false) @RequestParam(required = false) String _rows,
      @ApiParam(value = "Name of field for ordering the results", required = false) @RequestParam(required = false) String _sort,
      @ApiParam(value = "Sorting direction ascending (asc) or descending (desc)", required = false) @RequestParam(required = false) String _direction,
      @ApiParam(value = "Parameters for subscription query", required = false) @RequestBody(required = false) SubscriptionDTO subscriptionDTO
  ) {
    Pageable pageable = WebControllerUtils.parsePageParams(_rows, _page, _sort, _direction);

    Page<SubscriptionEntity> subscriptionEntityPage = subscriptionService
        .listSubscriptions(subscriptionDTO, pageable);
    if (subscriptionEntityPage != null && subscriptionEntityPage.getNumberOfElements() > 0) {
      ModelMapper modelMapper = new ModelMapper();
      List<SubscriptionDTO> subscriptionDTOList = subscriptionEntityPage.getContent().stream()
          .map(new Function<SubscriptionEntity, SubscriptionDTO>() {
            @Override
            public SubscriptionDTO apply(SubscriptionEntity subscriptionEntity) {
              SubscriptionDTO subscriptionDTO = modelMapper
                  .map(subscriptionEntity, SubscriptionDTO.class);
              return subscriptionDTO;
            }
          }).collect(Collectors.toList());

      HttpHeaders responseHeaders = new HttpHeaders();
      responseHeaders
          .set(ApplicationConstants.HEADER_TOTAL_COUNT, Long.toString(subscriptionEntityPage.getTotalElements()));

      return new ResponseEntity<>(subscriptionDTOList, responseHeaders, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
  }
}
