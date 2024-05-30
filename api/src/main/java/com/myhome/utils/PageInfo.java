package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that provides information about the number of pages, page size,
 * total pages, and total elements of a given Pageable and Page. The class offers a
 * constructor for creating instances of the class from a Pageable and a Page object,
 * and also includes methods for generating a `PageInfo` object containing information
 * about the current page being processed.
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
   * generates a `PageInfo` object containing information about the number of pages,
   * page size, total pages, and total elements for a given `Pageable` and `Page`.
   * 
   * @param pageable pagination information for the current page of data, providing the
   * page number, page size, total pages, and total elements.
   * 
   * 	- `pageNumber`: A page number that represents where the current page falls in
   * relation to the total number of pages available for the resource being paginated.
   * This value is typically represented as an integer between 1 and the maximum value
   * that can be stored in a variable, inclusive.
   * 	- `pageSize`: The size of each page of results returned by the server for the
   * current query. This value is typically represented as an integer between 0 and the
   * maximum value that can be stored in a variable, inclusive.
   * 	- `totalPages`: An estimate of the total number of pages available for the resource
   * being paginated. This value is typically represented as an integer between 1 and
   * infinity, inclusive.
   * 	- `totalElements`: The total number of elements returned by the server for the
   * current query. This value is typically represented as an integer between 0 and
   * infinity, inclusive.
   * 
   * @param page current page being processed, providing the total number of elements
   * and pages for the corresponding page of data.
   * 
   * 	- `getPageNumber()`: The page number that corresponds to the input `pageable`.
   * 	- `getPageSize()`: The number of elements in each page of the input `pageable`.
   * 	- `getTotalPages()`: The total number of pages that can be deserialized from the
   * input `pageable`.
   * 	- `getTotalElements()`: The total number of elements that can be deserialized
   * from the input `pageable`.
   * 
   * @returns a `PageInfo` object containing information about the current page of data.
   * 
   * 	- `pageNumber`: The page number of the pageable that the page belongs to.
   * 	- `pageSize`: The size of each page in the pageable.
   * 	- `totalPages`: The total number of pages in the pageable.
   * 	- `totalElements`: The total number of elements in the pageable.
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
