# Payments

## Payment Providers

### MockPaymentProvider (Default)

Always succeeds. Used for development and testing. No external calls.

### RazorpayPaymentProvider (TEST Mode)

Enabled when `RAZORPAY_KEY_ID` and `RAZORPAY_KEY_SECRET` are set. Uses Razorpay's TEST mode.

## Payment Flow

```
1. Student requests to enroll in a paid course
2. Server creates an order (price computed from DB, never from client)
3. Student applies coupon (optional)
4. Server calculates final price
5. Server creates payment intent via provider
6. Provider processes payment
7. Server verifies payment (signature verification for Razorpay)
8. On success: server creates enrollment
9. Server sends notification
```

## Idempotency

- Orders have an `idempotency_key` (unique)
- Payments have an `idempotency_key` (unique)
- Duplicate requests with the same key return the existing order/payment

## Refunds

- Admin-initiated only
- Refund amount cannot exceed payment amount
- Refund status: PENDING → COMPLETED/FAILED
- **Access rule**: Refunded students keep course access (documented policy)

## Order States

```
PENDING → COMPLETED (payment successful)
PENDING → FAILED (payment failed)
```

## Payment States

```
PENDING → SUCCESS (verified)
PENDING → FAILED (verification failed)
```

## Webhook Endpoint

```
POST /api/v1/payments/webhook
```

Verifies webhook signature before processing. Duplicate webhooks are ignored (idempotency key).

## Security

- Price is always computed server-side from the database
- Client never sends price information
- Payment signatures are verified before creating enrollment
- No enrollment without successful payment
- No double enrollment (unique constraint on student+course)
