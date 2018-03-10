package com.iscreate.mobile.utils;

import com.iscreate.mobile.widget.Point2D;

public class MathUtil {
	/**
	 * 计算两个点之间的距离
	 */
	public double getDistanceForTwoPoint(double x1, double y1, double x2,
			double y2) {
		return (Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)));
	}

	/**
	 * 一元二次方程deta
	 */
	public double getDeta(double a, double b, double c) {
		return (b * b - 4 * a * c);
	}

	/**
	 * Unitary quadratic equation 获取一元二次方程的解
	 * 
	 * @param a
	 *            不能为0
	 */
	public Point2D getUQESolution(double a, double b, double c) {
		if (a != 0) {
			Point2D p = new Point2D();
			double deta = getDeta(a, b, c);
			if (deta >= 0) {
				p.x = (-b + Math.sqrt(deta)) / (2 * a);
				p.y = (deta == 0) ? p.x : ((-b - Math.sqrt(deta)) / (2 * a));
				return (p);
			}
		}
		return (null);
	}

	/**
	 * 计算两个圆的交点
	 * 
	 * 设w=(r1*r1-r2*r2-a1*a1+a2*a2-b1*b1+b2*b2+2*b1-2*b2)/(2*a2-2*a1)
	 * 则计算两个圆交点的方程转换为x = wy 带入方程得到(w*w+1)*y*y -
	 * (2*a1*e+2*b1)*y+(a1*a1+b1*b1-r1*r1)=0 然后根据一元二次方程的求解
	 * 
	 * @param r1
	 *            圆1的半径
	 * @param a1
	 *            圆1坐标x,不能等于a2
	 * @param b1
	 *            圆1坐标y
	 * @param r2
	 *            圆2的半径
	 * @param a2
	 *            圆2坐标x
	 * @param b2
	 *            圆2坐标y
	 * @return 返回两个圆交点的坐标
	 */
	public Point2D[] getIntersectionForTwoCircle(double r1, double a1,
			double b1, double r2, double a2, double b2) {
		if (a1 != a2) {
			// (x-a1)*(x-a1)+(y-b1)*(y-b1)=r1*r1
			// (x-a2)*(x-a2)+(y-b2)*(y-b2)=r2*r2
			// 下式减上式得
			// x = v * y + w
			double w = (r1 * r1 - r2 * r2 - a1 * a1 + a2 * a2 - b1 * b1 + b2
					* b2)
					/ (2 * (a2 - a1));
			double v = (b1 - b2) / (a2 - a1);
			double a = (v * v + 1);
			double b = (2 * w * v - 2 * a1 * v - 2 * b1);
			double c = (w * w - r1 * r1 + a1 * a1 - 2 * a1 * w + b1 * b1);
			// a*y*y+b*y+c=0
			Point2D p = new MathUtil().getUQESolution(a, b, c);
			if (p != null) {
				Point2D[] intersections = new Point2D[(p.x != p.y) ? 2 : 1];
				intersections[0] = new Point2D();
				// 第一个交点
				intersections[0].y = p.x;
				intersections[0].x = w + v * intersections[0].y;
				if (p.x != p.y) {
					intersections[1] = new Point2D();
					// 第二个交点
					intersections[1].y = p.y;
					intersections[1].x = w + v * intersections[1].y;
				}
				return (intersections);
			}
		}
		return (null);
	}

	/**
	 * 计算两个圆的交点组成的直线，与另外一个圆的交点
	 * 
	 * @param r
	 *            第三个圆的半径
	 * @param r_x
	 *            第三个圆坐标x
	 * @param r_y
	 *            第三个圆坐标y
	 * @param x0
	 *            直线的两个点x,不能等于x1
	 * @param y0
	 *            直线的两个点y
	 * @param x1
	 *            直线的两个点x
	 * @param y1
	 *            直线的两个点y
	 */
	public Point2D[] getIntersectionForCircleAndLine(double r, double r_x,
			double r_y, double x0, double y0, double x1, double y1) {
		if (x0 != x1) {
			// y=kx+b
			double k = (y0 - y1) / (x0 - x1);
			double b = y0 - k * x0;
			return (getIntersectionForCircleAndLine(r, r_x, r_y, k, b));
		} else {
			double deta = r * r - Math.pow(x0 - r_x, 2);
			if (deta >= 0) {
				Point2D[] intersections = new Point2D[(deta == 0) ? 1 : 2];
				intersections[0] = new Point2D();
				// 第一个交点
				intersections[0].x = x0;
				intersections[0].y = r_y + Math.sqrt(deta);
				if (deta != 0) {
					intersections[1] = new Point2D();
					// 第二个交点
					intersections[1].x = x0;
					intersections[1].y = r_y - Math.sqrt(deta);
				}
				return (intersections);
			}
		}
		return (null);
	}

	public Point2D[] getIntersectionForCircleAndLine(double r, double r_x,
			double r_y, double k, double b) {
		double a1 = 1 + k * k;
		double b1 = -2 * (r_x + k * r_y - k * b);
		double c1 = r_x * r_x + r_y * r_y - 2 * b * r_y - r * r + b * b;
		Point2D p = new MathUtil().getUQESolution(a1, b1, c1);
		if (p != null) {
			Point2D[] intersections = new Point2D[(p.x == p.y) ? 1 : 2];
			intersections[0] = new Point2D();
			// 第一个交点
			intersections[0].x = p.x;
			intersections[0].y = k * intersections[0].x + b;
			if (p.x != p.y) {
				intersections[1] = new Point2D();
				// 第二个交点
				intersections[1].x = p.y;
				intersections[1].y = k * intersections[1].x + b;
			}
			return (intersections);
		}
		return (null);
	}

	/**
	 * 矩形和直线的交点
	 */
	public Point2D[] getIntersectionForRectAndLine(double left, double top,
			double right, double bottom, double x0, double y0, double x1,
			double y1) {
		if (x0 == x1) {
			if ((x0 >= left) && (x0 <= right)) {
				Point2D[] intersections = new Point2D[(bottom == top) ? 1 : 2];
				intersections[0] = new Point2D();
				// 第一个交点
				intersections[0].x = x0;
				intersections[0].y = top;
				if (bottom != top) {
					intersections[1] = new Point2D();
					// 第二个交点
					intersections[1].x = x0;
					intersections[1].y = bottom;
				}
				return (intersections);
			}
		} else {
			if (y0 == y1) {
				if ((y0 >= top) && (y0 <= bottom)) {
					Point2D[] intersections = new Point2D[(left == right) ? 1
							: 2];
					intersections[0] = new Point2D();
					// 第一个交点
					intersections[0].x = left;
					intersections[0].y = y0;
					if (left != right) {
						intersections[1] = new Point2D();
						// 第二个交点
						intersections[1].x = right;
						intersections[1].y = y0;
					}
					return (intersections);
				}
			} else {
				final double k = (y0 - y1) / (x0 - x1);
				final double b = y0 - k * x0;
				Point2D point1 = null;
				Point2D point2 = null;
				double x = left;
				double y = k * x + b;
				if ((y >= top) && (y <= bottom)) {
					point1 = Point2D.getPoint(x, y);
				}
				x = right;
				y = k * x + b;
				if ((y >= top) && (y <= bottom)) {
					if (point1 == null) {
						point1 = Point2D.getPoint(x, y);
					} else {
						point2 = Point2D.getPoint(x, y);
					}
				}
				if ((point1 == null) || (point2 == null)) {
					y = top;
					x = (y - b) / k;
					if ((x >= left) && (x <= right)) {
						if (point1 == null) {
							point1 = Point2D.getPoint(x, y);
						} else {
							point2 = Point2D.getPoint(x, y);
						}
					}
				}
				if ((point1 == null) || (point2 == null)) {
					y = bottom;
					x = (y - b) / k;
					if ((x >= left) && (x <= right)) {
						if (point1 == null) {
							point1 = Point2D.getPoint(x, y);
						} else {
							point2 = Point2D.getPoint(x, y);
						}
					}
				}
				if ((point1 != null) || (point2 != null)) {
					Point2D[] intersections = new Point2D[(point2 == null) ? 1
							: 2];
					intersections[0] = point1;
					// 第一个交点
					if (point2 != null) {
						intersections[1] = point2;
						// 第二个交点
					}
					return (intersections);
				}
			}
		}
		return (null);
	}

	/**
	 * 获取圆上的点
	 */
	public Point2D getCirclePoint(double cx, double cy, double cr, double radian) {
		double d = 2 * cr * Math.sin(radian / 2);
		double x = ((((cr * cr) - (d * d)) / cr) + (2 * cx) + cr) / 2;
		if (x > (cx + cr)) {
			x = cx + cr;
		}
		if (x < (cx - cr)) {
			x = cx - cr;
		}
		double deta = cr * cr - Math.pow(x - cx, 2);
		if (deta < 0) {
			deta = 0;
		}
		double detay = Math.sqrt(deta);
		double y = cy
				+ (((((int) ((radian % (Math.PI * 2)) / Math.PI)) == 1) ? -1
						: 1) * detay);
		return (Point2D.getPoint(x, y));
	}
}