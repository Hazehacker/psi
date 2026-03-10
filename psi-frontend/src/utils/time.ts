/**
 * 格式化日期时间为 "yyyy-mm-dd hh:mm:ss" 格式
 * @param date - 日期对象、日期字符串或null
 * @returns 格式化后的日期时间字符串，如果日期无效则返回 '-'
 */
export function formatDateTime(date: Date | null): string {
  if (!date) return '-'

  const d = new Date(date)
  if (isNaN(d.getTime())) return '-'

  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')

  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}
