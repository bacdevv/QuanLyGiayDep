<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" encoding="UTF-8" indent="yes"/>
  <xsl:template match="/phieuDoiTra">
    <html lang="vi"><head><meta charset="UTF-8"/>
    <title>Phiếu đổi trả <xsl:value-of select="maPDT"/></title>
    <style>
      body{font-family:Arial,sans-serif;max-width:800px;margin:32px auto;color:#222;font-size:14px}
      h1{text-align:center;font-size:24px}.sub{text-align:center;color:#666;margin-bottom:20px}
      .info{display:grid;grid-template-columns:1fr 1fr;gap:12px;margin:25px 0}
      table{border-collapse:collapse;width:100%;margin:16px 0}th,td{padding:10px;border:1px solid #bbb}
      th{background:#f2f2f2}.num{text-align:right}.reason{margin:20px 0}
      .actions{text-align:center;margin:28px}button{padding:10px 22px;cursor:pointer}
      @media print{.actions{display:none}body{margin:0;max-width:none}@page{margin:15mm}}
    </style></head><body>
      <h1>PHIẾU ĐỔI / TRẢ HÀNG</h1><div class="sub">CỬA HÀNG GIÀY DÉP</div>
      <div class="info">
        <div><b>Mã phiếu:</b> <xsl:value-of select="maPDT"/></div>
        <div><b>Loại:</b> <xsl:value-of select="loai"/></div>
        <div><b>Hóa đơn gốc:</b> <xsl:value-of select="hoaDonGoc"/></div>
        <div><b>Hóa đơn mới:</b> <xsl:choose><xsl:when test="normalize-space(hoaDonMoi) != ''"><xsl:value-of select="hoaDonMoi"/></xsl:when><xsl:otherwise>Không có</xsl:otherwise></xsl:choose></div>
        <div><b>Nhân viên:</b> <xsl:value-of select="tenNhanVien"/></div>
        <div><b>Ngày lập:</b> <xsl:value-of select="translate(substring(ngayLap,1,19),'T',' ')"/></div>
      </div>
      <div class="reason"><b>Lý do:</b> <xsl:value-of select="lyDo"/></div>
      <table><thead><tr><th>STT</th><th>Sản phẩm</th><th>Size</th><th>Màu</th><th class="num">Số lượng đổi/trả</th><th class="num">Nhập lại kho</th></tr></thead>
        <tbody><xsl:for-each select="chiTiet/dong"><tr>
          <td><xsl:value-of select="position()"/></td>
          <td><xsl:value-of select="tenSP"/><div style="color:#777;font-size:11px"><xsl:value-of select="maCTSP"/></div></td>
          <td><xsl:value-of select="kichCo"/></td><td><xsl:value-of select="mauSac"/></td>
          <td class="num"><xsl:value-of select="soLuong"/></td><td class="num"><xsl:value-of select="soLuongNhapLai"/></td>
        </tr></xsl:for-each></tbody>
      </table>
      <div style="margin-top:38px;text-align:right">Nhân viên lập phiếu: __________________</div>
      <div class="actions"><button onclick="window.print()">In chi tiết đổi/trả</button></div>
    </body></html>
  </xsl:template>
</xsl:stylesheet>
