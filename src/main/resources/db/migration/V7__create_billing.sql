CREATE TABLE membership_plans (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id UUID NOT NULL REFERENCES tenants(id),
  name VARCHAR(150) NOT NULL,
  price NUMERIC(12,2) NOT NULL CHECK (price >= 0),
  currency_code CHAR(3) NOT NULL REFERENCES currencies(code),
  duration_days INT NOT NULL CHECK (duration_days > 0),
  is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE subscriptions (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id UUID NOT NULL REFERENCES tenants(id),
  member_id UUID NOT NULL REFERENCES members(id),
  membership_plan_id UUID NOT NULL REFERENCES membership_plans(id),
  starts_on DATE,
  ends_on DATE,
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
);

CREATE TABLE payments (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id UUID NOT NULL REFERENCES tenants(id),
  subscription_id UUID NOT NULL REFERENCES subscriptions(id),
  provider VARCHAR(30) NOT NULL,
  amount NUMERIC(12,2) NOT NULL CHECK (amount >= 0),
  currency_code CHAR(3) NOT NULL REFERENCES currencies(code),
  method VARCHAR(50),
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  reference VARCHAR(100) NOT NULL UNIQUE,
  paid_at TIMESTAMPTZ
);

CREATE INDEX idx_membership_plans_tenant ON membership_plans(tenant_id);
CREATE INDEX idx_subscriptions_tenant ON subscriptions(tenant_id);
CREATE INDEX idx_subscriptions_member ON subscriptions(member_id);
CREATE INDEX idx_payments_tenant ON payments(tenant_id);
CREATE INDEX idx_payments_subscription ON payments(subscription_id);