package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * provides information about the number of pages, page size, total pages, and total
 * elements of a given Pageable and Page. The class offers a constructor for creating
 * instances of the class from a Pageable and a Page object, and also provides methods
 * for generating a `PageInfo` object containing information about the current page
 * being processed, including the page number and size, as well as the total number
 * of pages and elements available in the input.
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
   * takes a `pageable` and a `page` as input, and returns a `PageInfo` object containing
   * information about the page number, size, total pages, and total elements.
   * 
   * @param pageable Pageable object containing the information about the current page
   * of data to be processed.
   * 
   * 1/ Page number: The page number associated with the current page being processed.
   * 2/ Page size: The number of elements in a page of the collection.
   * 3/ Total pages: The total number of pages in the entire collection.
   * 4/ Total elements: The total number of elements in the entire collection.
   * 
   * @param page current page being processed, providing information on its number,
   * size, total pages, and total elements.
   * 
   * 	- The first parameter is an instance of `Pageable`.
   * 	- The second parameter is an instance of `Page`.
   * 	- The return value is a new instance of `PageInfo`, which contains information
   * about the page being processed.
   * 
   * @returns a `PageInfo` object containing page-related metadata.
   * 
   * 	- pageable.getPageNumber(): The number of the current page being displayed.
   * 	- pageable.getPageSize(): The number of elements displayed on each page.
   * 	- page.getTotalPages(): The total number of pages in the result set.
   * 	- page.getTotalElements(): The total number of elements in the result set.
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
