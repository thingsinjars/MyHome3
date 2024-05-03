package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that contains information about a page of results in a larger
 * dataset. It has four fields: currentPage, pageLimit, totalPages, and totalElements.
 * The class also provides a constructor for creating instances of the class from a
 * Pageable object and a Page object.
 */
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
   * Generates high-quality documentation for code given to it, providing information
   * about a pageable and its corresponding page, including the page number, size, total
   * pages, and total elements.
   * 
   * @param pageable pagination information for the given `Page` object, providing the
   * page number, page size, total pages, and total elements.
   * 
   * @param page current page being accessed or manipulated in the application, providing
   * information on its position and size within the larger dataset.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
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
