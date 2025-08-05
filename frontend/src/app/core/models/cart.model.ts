export interface CartItem {
  id: number
  quantity: number
  unitPrice: number
  productName: string
  productId: number
}

export interface Cart {
  id: number
  createdDate: string
  updatedDate: string
  items: CartItem[]
}
