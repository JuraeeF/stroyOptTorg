(function () {
  "use strict";

  var state = {
    categories: [],
    products: [],
    cart: readCart(),
    page: 0,
    totalPages: 0,
    totalElements: 0,
    categoryId: "",
    query: "",
    sort: "newest",
    token: localStorage.getItem("stroy.token") || "",
    user: null,
    authMode: "login",
    pendingCheckout: false,
    toastTimer: null,
    searchTimer: null
  };

  var el = {
    categoryChips: document.getElementById("category-chips"),
    grid: document.getElementById("product-grid"),
    result: document.getElementById("result-label"),
    notice: document.getElementById("catalog-notice"),
    empty: document.getElementById("empty-state"),
    pagination: document.getElementById("pagination"),
    pageLabel: document.getElementById("page-label"),
    prev: document.getElementById("page-prev"),
    next: document.getElementById("page-next"),
    sort: document.getElementById("sort-select"),
    searchForm: document.getElementById("search-form"),
    searchInput: document.getElementById("search-input"),
    cartButton: document.getElementById("cart-button"),
    cartCount: document.getElementById("cart-count"),
    drawer: document.getElementById("cart-drawer"),
    cartItems: document.getElementById("cart-items"),
    cartEmpty: document.getElementById("cart-empty"),
    cartFooter: document.getElementById("cart-footer"),
    cartTotal: document.getElementById("cart-total"),
    overlay: document.getElementById("overlay"),
    account: document.getElementById("account-button"),
    accountLabel: document.getElementById("account-label"),
    authDialog: document.getElementById("auth-dialog"),
    authForm: document.getElementById("auth-form"),
    authError: document.getElementById("auth-error"),
    authSubmit: document.getElementById("auth-submit"),
    authTitle: document.getElementById("auth-title"),
    checkoutDialog: document.getElementById("checkout-dialog"),
    checkoutForm: document.getElementById("checkout-form"),
    checkoutError: document.getElementById("checkout-error"),
    toast: document.getElementById("toast")
  };

  function readCart() {
    try {
      var saved = JSON.parse(localStorage.getItem("stroy.cart") || "[]");
      return Array.isArray(saved) ? saved : [];
    } catch (error) {
      return [];
    }
  }

  function escapeHtml(value) {
    return String(value == null ? "" : value).replace(/[&<>"']/g, function (char) {
      return { "&": "&amp;", "<": "&lt;", ">": "&gt;", "\"": "&quot;", "'": "&#39;" }[char];
    });
  }

  function formatPrice(value) {
    return new Intl.NumberFormat("ru-RU", { maximumFractionDigits: 0 }).format(Number(value) || 0) + " сум";
  }

  function iconFor(product) {
    var text = ((product.categoryName || "") + " " + (product.name || "")).toLowerCase();
    if (/цемент|штукатур|смес|шпак|мешок/.test(text)) {
      return '<svg viewBox="0 0 48 48" aria-hidden="true"><path d="M14 8h20l4 8-3 25H13L10 16z"/><path d="M14 8l-4 8h28l-4-8M18 8l2 8m10-8-2 8M17 24h14m-12 5h10"/><path d="M18 35h12"/></svg>';
    }
    if (/кирпич|блок/.test(text)) {
      return '<svg viewBox="0 0 48 48" aria-hidden="true"><path d="M7 13h15v10H7zm19 0h15v10H26zM7 27h15v10H7zm19 0h15v10H26z"/><path d="M22 18h4M22 32h4"/></svg>';
    }
    if (/арматур|металл|труб|профил/.test(text)) {
      return '<svg viewBox="0 0 48 48" aria-hidden="true"><path d="m9 36 25-25 5 5-25 25z"/><path d="m17 37 19-19M12 32l19-19M9 36l3 5 5-1M34 11l5-1 1 5"/><path d="M13 34 34 13"/></svg>';
    }
    if (/кров|профнастил|череп/.test(text)) {
      return '<svg viewBox="0 0 48 48" aria-hidden="true"><path d="m5 22 19-15 19 15v19H5z"/><path d="M13 19v22m7-27v27m7-27v27m7-22v22M5 22h38"/></svg>';
    }
    if (/краск|лак|грунт/.test(text)) {
      return '<svg viewBox="0 0 48 48" aria-hidden="true"><path d="M14 17h20l3 24H11zM17 17v-5h14v5"/><path d="M18 12V8h12v4m-10 15h12"/><path d="M22 35h4"/></svg>';
    }
    return '<svg viewBox="0 0 48 48" aria-hidden="true"><path d="m24 5 17 9.5v19L24 43 7 33.5v-19z"/><path d="m7 14.5 17 10 17-10M24 24.5V43m-8-33.5 17 10v9"/></svg>';
  }

  function notify(message) {
    el.toast.textContent = message;
    el.toast.classList.add("is-visible");
    window.clearTimeout(state.toastTimer);
    state.toastTimer = window.setTimeout(function () {
      el.toast.classList.remove("is-visible");
    }, 3000);
  }

  function setError(element, message) {
    element.textContent = message || "";
    element.hidden = !message;
  }

  async function api(path, options) {
    options = options || {};
    var headers = Object.assign({ "Accept": "application/json" }, options.headers || {});
    if (options.body && !headers["Content-Type"]) headers["Content-Type"] = "application/json";
    if (state.token) headers.Authorization = "Bearer " + state.token;
    var response = await fetch(path, Object.assign({}, options, { headers: headers }));
    if (response.status === 204) return null;
    var data = null;
    var type = response.headers.get("content-type") || "";
    if (type.indexOf("application/json") >= 0) data = await response.json();
    if (!response.ok) {
      var message = data && (data.message || data.error);
      if (!message && data && data.errors) {
        message = Array.isArray(data.errors) ? data.errors.map(function (item) { return item.message || item; }).join(", ") : String(data.errors);
      }
      throw new Error(message || ("Ошибка запроса (" + response.status + ")"));
    }
    return data;
  }

  function renderCategories() {
    var active = String(state.categoryId || "");
    var html = '<button class="category-chip' + (active === "" ? " is-active" : "") + '" type="button" data-category="">Все товары</button>';
    state.categories.forEach(function (category) {
      var id = String(category.id);
      html += '<button class="category-chip' + (active === id ? " is-active" : "") + '" type="button" data-category="' + escapeHtml(id) + '">' + escapeHtml(category.name) + "</button>";
    });
    el.categoryChips.innerHTML = html;
  }

  function productCard(product, index) {
    var min = Math.max(1, Number(product.minOrderQuantity) || 1);
    var stock = Math.max(0, Number(product.stock) || 0);
    var canOrder = stock >= min;
    var visual = product.imageUrl
      ? '<img src="' + escapeHtml(product.imageUrl) + '" alt="' + escapeHtml(product.name) + '" loading="lazy">'
      : '<div class="product-symbol">' + iconFor(product) + "</div>";
    return '<article class="product-card">' +
      '<div class="product-visual" data-tone="' + (index % 5) + '">' +
      '<span class="stock-pill">' + (stock > 0 ? "В наличии" : "Под заказ") + "</span>" + visual + "</div>" +
      '<div class="product-body">' +
      '<div class="product-category">' + escapeHtml(product.categoryName || "Каталог") + "</div>" +
      '<h3 class="product-title">' + escapeHtml(product.name) + "</h3>" +
      '<div class="product-meta"><span>Арт. ' + escapeHtml(product.sku || "—") + "</span><span>Остаток: " + stock + "</span></div>" +
      '<div class="product-price"><strong>' + formatPrice(product.price) + "</strong><span>/ " + escapeHtml(product.unit || "шт") + "</span></div>" +
      '<div class="product-card-bottom"><span class="min-order">Мин. заказ:<br>' + min + " " + escapeHtml(product.unit || "шт") + "</span>" +
      '<button class="add-button" type="button" data-add="' + escapeHtml(product.id) + '" aria-label="Добавить ' + escapeHtml(product.name) + ' в корзину" ' + (canOrder ? "" : "disabled") + '>+</button></div>' +
      "</div></article>";
  }

  function renderProducts() {
    el.grid.innerHTML = "";
    el.empty.hidden = state.products.length > 0;
    if (!state.products.length) {
      el.result.textContent = state.totalElements ? "Товары не найдены" : "Каталог пока пуст";
      el.pagination.hidden = true;
      return;
    }
    el.result.textContent = state.totalElements + (state.totalElements === 1 ? " товар" : " товаров");
    el.grid.innerHTML = state.products.map(productCard).join("");
    el.pagination.hidden = state.totalPages <= 1;
    el.pageLabel.textContent = (state.page + 1) + " / " + state.totalPages;
    el.prev.disabled = state.page <= 0;
    el.next.disabled = state.page >= state.totalPages - 1;
  }

  async function loadCategories() {
    try {
      state.categories = await api("/api/categories") || [];
      renderCategories();
    } catch (error) {
      el.categoryChips.innerHTML = '<button class="category-chip is-active" type="button" data-category="">Все товары</button>';
    }
  }

  async function loadProducts() {
    el.notice.hidden = true;
    el.grid.innerHTML = '<div class="product-skeleton"></div><div class="product-skeleton"></div><div class="product-skeleton"></div><div class="product-skeleton"></div>';
    el.empty.hidden = true;
    el.result.textContent = "Загружаем каталог…";
    var params = new URLSearchParams({ page: String(state.page), size: "12", sort: state.sort });
    if (state.categoryId) params.set("categoryId", state.categoryId);
    if (state.query) params.set("q", state.query);
    try {
      var result = await api("/api/products?" + params.toString());
      state.products = result && Array.isArray(result.content) ? result.content : [];
      state.totalPages = Number(result && result.totalPages) || 0;
      state.totalElements = Number(result && result.totalElements) || 0;
      renderProducts();
      renderCategories();
    } catch (error) {
      state.products = [];
      state.totalPages = 0;
      state.totalElements = 0;
      el.grid.innerHTML = "";
      el.result.textContent = "Каталог недоступен";
      el.empty.hidden = true;
      el.pagination.hidden = true;
      el.notice.textContent = "Не удалось загрузить товары. Запустите Spring Boot и проверьте подключение к базе данных.";
      el.notice.hidden = false;
    }
  }

  function saveCart() {
    localStorage.setItem("stroy.cart", JSON.stringify(state.cart));
    renderCart();
  }

  function cartQuantity() {
    return state.cart.reduce(function (sum, item) { return sum + Number(item.quantity || 0); }, 0);
  }

  function renderCart() {
    el.cartCount.textContent = String(cartQuantity());
    el.cartEmpty.hidden = state.cart.length > 0;
    el.cartFooter.hidden = state.cart.length === 0;
    el.cartItems.innerHTML = state.cart.map(function (item) {
      var min = Math.max(1, Number(item.minOrderQuantity) || 1);
      return '<div class="cart-row">' +
        '<div class="cart-thumb">' + iconFor(item) + "</div>" +
        '<div><div class="cart-product-name">' + escapeHtml(item.name) + "</div>" +
        '<div class="cart-product-price">' + formatPrice(item.price) + " / " + escapeHtml(item.unit || "шт") + "</div>" +
        '<div class="quantity-control"><button type="button" data-qty="down" data-id="' + escapeHtml(item.id) + '" aria-label="Уменьшить количество">−</button><span>' + Number(item.quantity) + '</span><button type="button" data-qty="up" data-id="' + escapeHtml(item.id) + '" aria-label="Увеличить количество">+</button></div></div>' +
        '<div class="cart-line-total">' + formatPrice(Number(item.price) * Number(item.quantity)) + "</div></div>";
    }).join("");
    el.cartTotal.textContent = formatPrice(state.cart.reduce(function (sum, item) { return sum + Number(item.price) * Number(item.quantity); }, 0));
  }

  function addToCart(id) {
    var product = state.products.find(function (item) { return String(item.id) === String(id); });
    if (!product) return;
    var min = Math.max(1, Number(product.minOrderQuantity) || 1);
    var stock = Number(product.stock) || 0;
    if (stock < min) {
      notify("Товар сейчас недоступен для заказа.");
      return;
    }
    var existing = state.cart.find(function (item) { return String(item.id) === String(id); });
    if (existing) {
      existing.quantity = Math.min(stock, Number(existing.quantity) + min);
      Object.assign(existing, product);
    } else {
      state.cart.push(Object.assign({}, product, { quantity: min }));
    }
    saveCart();
    notify("Товар добавлен в корзину");
  }

  function changeQuantity(id, direction) {
    var item = state.cart.find(function (entry) { return String(entry.id) === String(id); });
    if (!item) return;
    var min = Math.max(1, Number(item.minOrderQuantity) || 1);
    var stock = Number(item.stock) || 0;
    if (direction === "up") {
      item.quantity = Math.min(stock, Number(item.quantity) + min);
    } else if (Number(item.quantity) - min < min) {
      state.cart = state.cart.filter(function (entry) { return String(entry.id) !== String(id); });
    } else {
      item.quantity = Number(item.quantity) - min;
    }
    saveCart();
  }

  function showLayer(target) {
    closeLayer();
    el.overlay.hidden = false;
    target.hidden = false;
    document.body.classList.add("locked");
  }

  function closeLayer() {
    el.overlay.hidden = true;
    el.drawer.hidden = true;
    el.authDialog.hidden = true;
    el.checkoutDialog.hidden = true;
    document.body.classList.remove("locked");
  }

  function openAuth(mode) {
    setAuthMode(mode || state.authMode);
    setError(el.authError, "");
    showLayer(el.authDialog);
  }

  function setAuthMode(mode) {
    state.authMode = mode;
    var isRegister = mode === "register";
    document.querySelectorAll("[data-auth-mode]").forEach(function (button) {
      button.classList.toggle("is-active", button.getAttribute("data-auth-mode") === mode);
    });
    document.querySelectorAll(".field-register").forEach(function (field) { field.hidden = !isRegister; });
    document.querySelector('[name="fullName"]').required = isRegister;
    document.querySelector('[name="password"]').autocomplete = isRegister ? "new-password" : "current-password";
    el.authTitle.textContent = isRegister ? "Создать аккаунт" : "Войти в аккаунт";
    el.authSubmit.textContent = isRegister ? "Зарегистрироваться" : "Войти";
  }

  function openCheckout() {
    if (!state.cart.length) return;
    if (!state.token) {
      state.pendingCheckout = true;
      openAuth("login");
      return;
    }
    var form = el.checkoutForm.elements;
    if (state.user) {
      form.customerName.value = state.user.fullName || "";
      form.phone.value = state.user.phone || "";
    }
    setError(el.checkoutError, "");
    showLayer(el.checkoutDialog);
  }

  function setSignedIn(auth) {
    state.token = auth.token;
    state.user = auth.user || null;
    localStorage.setItem("stroy.token", state.token);
    el.accountLabel.textContent = state.user && state.user.fullName ? state.user.fullName.split(" ")[0] : "Аккаунт";
  }

  async function restoreSession() {
    if (!state.token) return;
    try {
      state.user = await api("/api/auth/me");
      el.accountLabel.textContent = state.user && state.user.fullName ? state.user.fullName.split(" ")[0] : "Аккаунт";
    } catch (error) {
      state.token = "";
      state.user = null;
      localStorage.removeItem("stroy.token");
      el.accountLabel.textContent = "Войти";
    }
  }

  el.categoryChips.addEventListener("click", function (event) {
    var button = event.target.closest("[data-category]");
    if (!button) return;
    state.categoryId = button.getAttribute("data-category") || "";
    state.page = 0;
    renderCategories();
    loadProducts();
  });

  el.grid.addEventListener("click", function (event) {
    var button = event.target.closest("[data-add]");
    if (button) addToCart(button.getAttribute("data-add"));
  });

  el.searchForm.addEventListener("submit", function (event) {
    event.preventDefault();
    state.query = el.searchInput.value.trim();
    state.page = 0;
    loadProducts();
    document.getElementById("catalog").scrollIntoView({ behavior: "smooth" });
  });

  el.searchInput.addEventListener("input", function () {
    window.clearTimeout(state.searchTimer);
    state.searchTimer = window.setTimeout(function () {
      state.query = el.searchInput.value.trim();
      state.page = 0;
      loadProducts();
    }, 350);
  });

  el.sort.addEventListener("change", function () {
    state.sort = el.sort.value;
    state.page = 0;
    loadProducts();
  });

  el.prev.addEventListener("click", function () {
    if (state.page > 0) { state.page -= 1; loadProducts(); }
  });

  el.next.addEventListener("click", function () {
    if (state.page < state.totalPages - 1) { state.page += 1; loadProducts(); }
  });

  document.getElementById("reset-filters").addEventListener("click", function () {
    state.categoryId = "";
    state.query = "";
    state.page = 0;
    el.searchInput.value = "";
    renderCategories();
    loadProducts();
  });

  el.cartButton.addEventListener("click", function () {
    renderCart();
    showLayer(el.drawer);
  });

  el.cartItems.addEventListener("click", function (event) {
    var button = event.target.closest("[data-qty]");
    if (button) changeQuantity(button.getAttribute("data-id"), button.getAttribute("data-qty"));
  });

  el.account.addEventListener("click", function () {
    if (state.token) {
      state.token = "";
      state.user = null;
      localStorage.removeItem("stroy.token");
      el.accountLabel.textContent = "Войти";
      notify("Вы вышли из аккаунта");
    } else {
      openAuth("login");
    }
  });

  el.overlay.addEventListener("click", closeLayer);
  document.querySelectorAll("[data-close]").forEach(function (button) { button.addEventListener("click", closeLayer); });
  document.addEventListener("keydown", function (event) { if (event.key === "Escape") closeLayer(); });
  document.getElementById("checkout-button").addEventListener("click", openCheckout);

  document.querySelectorAll("[data-auth-mode]").forEach(function (button) {
    button.addEventListener("click", function () { setAuthMode(button.getAttribute("data-auth-mode")); setError(el.authError, ""); });
  });

  el.authForm.addEventListener("submit", async function (event) {
    event.preventDefault();
    setError(el.authError, "");
    el.authSubmit.disabled = true;
    var form = new FormData(el.authForm);
    var body = state.authMode === "register"
      ? { fullName: form.get("fullName"), email: form.get("email"), phone: form.get("phone") || null, companyName: form.get("companyName") || null, password: form.get("password") }
      : { email: form.get("email"), password: form.get("password") };
    try {
      var auth = await api("/api/auth/" + state.authMode, { method: "POST", body: JSON.stringify(body) });
      setSignedIn(auth);
      closeLayer();
      notify("Вы вошли в аккаунт");
      if (state.pendingCheckout) {
        state.pendingCheckout = false;
        openCheckout();
      }
    } catch (error) {
      setError(el.authError, error.message || "Не удалось войти. Проверьте данные.");
    } finally {
      el.authSubmit.disabled = false;
    }
  });

  el.checkoutForm.addEventListener("submit", async function (event) {
    event.preventDefault();
    setError(el.checkoutError, "");
    var submit = el.checkoutForm.querySelector('button[type="submit"]');
    submit.disabled = true;
    var values = new FormData(el.checkoutForm);
    var payload = {
      customerName: values.get("customerName"),
      phone: values.get("phone"),
      address: values.get("address"),
      comment: values.get("comment") || "",
      items: state.cart.map(function (item) { return { productId: Number(item.id), quantity: Number(item.quantity) }; })
    };
    try {
      var order = await api("/api/orders", { method: "POST", body: JSON.stringify(payload) });
      state.cart = [];
      saveCart();
      closeLayer();
      el.checkoutForm.reset();
      notify("Заказ №" + order.id + " отправлен. Мы свяжемся с вами.");
    } catch (error) {
      setError(el.checkoutError, error.message || "Не удалось отправить заказ. Попробуйте ещё раз.");
    } finally {
      submit.disabled = false;
    }
  });

  renderCart();
  renderCategories();
  loadCategories();
  loadProducts();
  restoreSession();
})();