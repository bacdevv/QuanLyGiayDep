<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" encoding="UTF-8" indent="yes"/>
  <xsl:decimal-format name="vn" decimal-separator="," grouping-separator="."/>
  <xsl:template match="/hoaDon">
    <html lang="vi"><head><meta charset="UTF-8"/>
    <title>Hóa đơn <xsl:value-of select="maHD"/></title>
    <style>
      body{font-family:Arial,sans-serif;max-width:800px;margin:32px auto;color:#202020;font-size:14px}
      h1{text-align:center;font-size:25px;margin-bottom:3px} .sub{text-align:center;color:#666;margin-bottom:24px}
      .info{display:grid;grid-template-columns:1fr 1fr;gap:10px;margin:16px 0}
      table{border-collapse:collapse;width:100%;margin:15px 0}th,td{padding:10px;border:1px solid #bbb;text-align:left}
      th{background:#f0f0f0}.num{text-align:right;white-space:nowrap}.sum{margin-left:auto;width:330px}
      .sum div{display:flex;justify-content:space-between;padding:7px 0}
      .total{border-top:2px solid #222;font-size:18px;font-weight:bold}
      .actions{text-align:center;margin:22px}button{padding:10px 22px;cursor:pointer}
      .foot{text-align:center;margin-top:36px;color:#555}
      @media print{.actions{display:none}body{margin:0;max-width:none} @page{margin:15mm}}
    </style></head>
    <body>
      <h1>HÓA ĐƠN BÁN HÀNG</h1><div class="sub">CỬA HÀNG GIÀY DÉP</div>
      <div class="info">
        <div><b>Mã hóa đơn:</b> <xsl:value-of select="maHD"/></div>
        <div><b>Ngày lập:</b> <xsl:value-of select="translate(substring(ngayLap,1,19),'T',' ')"/></div>
        <div><b>Nhân viên:</b> <xsl:value-of select="tenNhanVien"/></div>
        <div><b>Thanh toán:</b> <xsl:value-of select="phuongThucThanhToan"/></div>
        <div><b>Khách hàng:</b> <xsl:value-of select="tenKH"/></div>
        <div><b>SĐT:</b> <xsl:value-of select="soDienThoai"/></div>
      </div>
      <table><thead><tr><th>STT</th><th>Sản phẩm</th><th>Size</th><th>Màu</th><th class="num">SL</th><th class="num">Đơn giá</th><th class="num">Thành tiền</th></tr></thead>
      <tbody><xsl:for-each select="chiTiet/dong"><tr>
        <td><xsl:value-of select="position()"/></td>
        <td><xsl:value-of select="tenSP"/><div style="font-size:11px;color:#777"><xsl:value-of select="maCTSP"/></div></td>
        <td><xsl:value-of select="kichCo"/></td><td><xsl:value-of select="mauSac"/></td>
        <td class="num"><xsl:value-of select="soLuong"/></td>
        <td class="num"><xsl:value-of select="format-number(number(donGia),'#.##0','vn')"/> đ</td>
        <td class="num"><xsl:value-of select="format-number(number(thanhTien),'#.##0','vn')"/> đ</td>
      </tr></xsl:for-each></tbody></table>
      <div class="sum">
        <div><span>Tiền hàng:</span><span><xsl:value-of select="format-number(number(tienHang),'#.##0','vn')"/> đ</span></div>
        <div><span>VAT (<xsl:value-of select="thue"/>%):</span><span><xsl:value-of select="format-number(number(tienThue),'#.##0','vn')"/> đ</span></div>
        <div class="total"><span>Tổng thanh toán:</span><span><xsl:value-of select="format-number(number(tongTien),'#.##0','vn')"/> đ</span></div>
      </div>
      <div class="foot">Cảm ơn quý khách và hẹn gặp lại!</div>
      <div class="actions"><button onclick="window.print()">In hóa đơn</button></div>
    </body></html>
  </xsl:template>
</xsl:stylesheet>
