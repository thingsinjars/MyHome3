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
   * generates high-quality documentation for given code, providing information on page
   * number, size, total pages, and total elements.
   * 
   * @param pageable pageable object that contains information about the current page
   * being processed, including its page number and size.
   * 
   * @param page current page of data being processed, providing the total elements and
   * number of pages for the `PageInfo` object to calculate and return.
   * 
   * @returns a `PageInfo` object containing page-related metadata.
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
