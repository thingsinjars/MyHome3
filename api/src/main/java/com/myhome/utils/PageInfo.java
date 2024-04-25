package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@EqualsAndHashCode
@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PageInfo {
  private final int currentPage;
  private final int pageLimit;
  private final int totalPages;
  private final long totalElements;

  /**
   * generates high-quality documentation for code given to it by returning a `PageInfo`
   * object containing information about the specified pageable and page.
   * 
   * @param pageable pagination information for thePage<?> object passed as an argument
   * to the function.
   * 
   * @param page current page being processed, providing its total elements and pages
   * count to construct the `PageInfo` object.
   * 
   * @returns a `PageInfo` object containing page-related information.
   */
  public static PageInfo of(Pageable pageable, Page<?> page) {
    return new PageInfo(
        pageable.getPageNumber(),
        pageable.getPageSize(),
        page.getTotalPages(),
        page.getTotalElements()
    );
  }
}
