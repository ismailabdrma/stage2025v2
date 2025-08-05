export interface OrderItem {
  id: number
  productId: number
  productName: string
  quantity: number
  unitPrice: number
  totalPrice: number
}

export interface ShippingAddress {
  firstName: string
  lastName: string
  address: string
  city: string
  postalCode: string
  country: string
  phone?: string
}

export interface Order {
  id: number
  userId: number
  customerName: string
  customerEmail: string
  orderDate: string
  status: "PENDING" | "CONFIRMED" | "SHIPPED" | "DELIVERED" | "CANCELLED"
  items: OrderItem[]
  shippingAddress: ShippingAddress
  subtotal: number
  shipping: number
  tax: number
  total: number
  createdDate: string
  updatedDate: string
}

export interface CreateOrderRequest {
  items: {
    productId: number
    quantity: number
  }[]
  shippingAddress: ShippingAddress
}

export interface OrderSummary {
  id: number
  orderDate: string
  status: string
  total: number
  itemCount: number
}
