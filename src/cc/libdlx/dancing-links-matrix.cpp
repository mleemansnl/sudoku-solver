#include "dancing-links-matrix.hpp"

// https://en.wikipedia.org/wiki/Dancing_Links
namespace dlx {
DancingLinksMatrix::DancingLinksMatrix()
    : root(std::make_unique<DancingLinkHeader>()) {}

auto DancingLinksMatrix::newHeader() -> DancingLinkHeader * {
  // Note that left of the doubly-linked list this->root always points to the end of the list
  // Therefore, adding to the left of this->root, adds the new item to the end of the list.
  std::unique_ptr<DancingLinkHeader> header = std::make_unique<DancingLinkHeader>();
  header->insertLeftOf(this->root.get());

  auto *header_ptr = header.get();
  this->headers.push_back(std::move(header));
  return header_ptr;
}

auto DancingLinksMatrix::getRoot() -> DancingLinkHeader * {
  return this->root.get();
}

}  // namespace dlx